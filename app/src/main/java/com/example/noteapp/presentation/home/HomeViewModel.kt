package com.example.noteapp.presentation.home

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.use_case.DeleteNoteUseCase
import com.example.noteapp.domain.use_case.GetNoteUseCase
import com.example.noteapp.domain.use_case.ScheduleNotifyUseCase
import com.example.noteapp.domain.use_case.SearchNoteUseCase
import com.example.noteapp.domain.use_case.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNotesUseCase: GetNoteUseCase,
    private val searchNoteUseCase: SearchNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val scheduleNotifyUseCase: ScheduleNotifyUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase


) : ViewModel() {


    val showSearchView = mutableStateOf(false)
    val searchText = mutableStateOf("")

    fun updateSearchText(value: String) {
        searchText.value = value
    }

    val isShowDatePicker = mutableStateOf(false)


    private val _listNote = MutableStateFlow<List<Note>>(emptyList())
    val listNote: StateFlow<List<Note>> = _listNote.asStateFlow()

    val isDarkTheme = mutableStateOf(false)
    private val _homeState = mutableStateOf(HomeState())

    val isListMode = mutableStateOf(false)


    val homeState: State<HomeState> = _homeState


    fun updateShowDatePicker(updateValue: Boolean) {

        isShowDatePicker.value = updateValue

    }


    fun toggleSearchView() {
        showSearchView.value = !showSearchView.value
    }

    fun toggleTheme() {
        isDarkTheme.value = !isDarkTheme.value
    }

    fun toggleListMode() {
        isListMode.value = !isListMode.value
    }

    private var isFirstLoad = true

    fun getAllNote() {

        Log.d("isFirstLoad", isFirstLoad.toString())
        if (isFirstLoad) {
            _homeState.value = HomeState(isLoading = true)
            isFirstLoad = false
        }

        viewModelScope.launch(Dispatchers.IO) {
            getNotesUseCase.getAllNote().collect { notes ->
                if (notes.isEmpty()) {
                    _homeState.value = HomeState(error = "Can't get data from database")
                } else {
                    _listNote.value = notes
                    _homeState.value = HomeState(
                        isLoading = false,
                        isSuccess = true
                    )
                }
            }
        }
    }


    fun searchNoteByTitle(title: String) {
        viewModelScope.launch(Dispatchers.IO) {

            searchNoteUseCase.searchNotesByTitle(title).collect { notes ->
                if (notes.isEmpty()) {
                    _listNote.value = notes
                } else {
                    _listNote.value = notes

                }

            }

        }

    }

    fun searchNoteByDate(date: String) {

        viewModelScope.launch(Dispatchers.IO) {
            searchNoteUseCase.searchNotesByDate(date).collect { notes ->
                if (notes.isEmpty()) {
                    _listNote.value = notes
                } else {
                    _listNote.value = notes

                }

            }

        }

    }

    val selectedDate = mutableStateOf("")

    fun updateSelectedDate(date: String) {
        selectedDate.value = date
    }


    private val _selectedNoteIds = mutableStateOf<Set<Long>>(emptySet())
    val selectedNoteIds: State<Set<Long>> = _selectedNoteIds

    // Bật/tắt chọn một note
    fun toggleSelection(noteId: Long) {
        _selectedNoteIds.value = if (_selectedNoteIds.value.contains(noteId)) {
            _selectedNoteIds.value - noteId // Bỏ chọn
        } else {
            _selectedNoteIds.value + noteId // Chọn thêm
        }
    }

    // Xóa hết các lựa chọn
    fun clearSelection() {
        _selectedNoteIds.value = emptySet()
    }
    fun deleteNotes(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val failedDeleteImages = mutableListOf<String>()
            val failedCancelNotifications = mutableListOf<String>()

            if (_selectedNoteIds.value.isEmpty()) {
                Log.d("CheckSelected", "Không có ghi chú nào được chọn.")
                return@launch
            }

            val noteIds = _selectedNoteIds.value.toList()
            val listImg = _listNote.value.filter { it.id in noteIds}.map { it.image }
            val deleteCount = deleteNoteUseCase.deleteNotesByIds(noteIds)

            if (deleteCount <= 0) {
                Log.e("DeleteNotes", "Xóa ghi chú thất bại.")
                return@launch
            }



            listImg.forEach { fileName  ->
                try {
                        updateNoteUseCase.deleteImage(fileName.toString())

                } catch (e: Exception) {
                    failedDeleteImages.add(fileName.toString())
                    Log.e("DeleteImageError", "Không thể xóa ảnh cho noteId: $fileName", e)
                }
            }

            // Xử lý hủy notification
            noteIds.forEach { noteId ->
                try {
                    scheduleNotifyUseCase.cancelNoteNotification(context, noteId.toString())
                } catch (e: Exception) {
                    failedCancelNotifications.add(noteId.toString())
                    Log.e("CancelNotificationError", "Không thể hủy thông báo cho noteId: $noteId", e)
                }
            }

            // Tổng kết lỗi
            if (failedDeleteImages.isNotEmpty()) {
                Log.e("Summary", "Xóa ảnh thất bại cho: $failedDeleteImages")
            }

            if (failedCancelNotifications.isNotEmpty()) {
                Log.e("Summary", "Hủy thông báo thất bại cho: $failedCancelNotifications")
            }

            Log.d("CheckSelected", "Xóa thành công ${deleteCount} ghi chú.")
        }
    }



}
