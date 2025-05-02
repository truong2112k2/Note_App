// HomeViewModel.kt
package com.example.noteapp.presentation.home.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.common.Constants
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,

    ) : ViewModel() {

    val uiState = HomeScreenFields()

    private val _listNote = MutableStateFlow<List<Note>>(emptyList())
    val listNote: StateFlow<List<Note>> = _listNote.asStateFlow()

    private val _getListState = MutableStateFlow(HomeState())
    val getListState: StateFlow<HomeState> = _getListState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()


    private var isFirstLoad = true

    init {
        viewModelScope.launch {
            searchQuery
                .debounce(400)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.isBlank()) {
                        getAllNote()
                    } else {
                        searchNoteByTitle(query)
                    }
                }
        }
        viewModelScope.launch {
            noteUseCases.dataStorageUseCase.getTheme().collect {
                uiState.isDarkTheme.value = it
            }
        }

    }

    fun updateSearchText(value: String) {
        uiState.searchText.value = value
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateShowDatePicker(updateValue: Boolean) {
        uiState.isShowDatePicker.value = updateValue
    }

    fun updateSelectedDate(date: String) {
        uiState.selectedDate.value = date
    }

    fun toggleSearchView() {
        uiState.showSearchView.value = !uiState.showSearchView.value
    }

    fun toggleTheme() {
        viewModelScope.launch {
            val newTheme = !uiState.isDarkTheme.value
            noteUseCases.dataStorageUseCase.saveTheme(newTheme)
            uiState.isDarkTheme.value = newTheme
        }
    }

    fun toggleListMode() {
        uiState.isListMode.value = !uiState.isListMode.value
//        if (uiState.isListMode.value == false) {
//            clearSelection()
//        }
    }

    fun toggleSelection(noteId: Long) {
        uiState.selectedNoteIds.value = if (uiState.selectedNoteIds.value.contains(noteId)) {
            uiState.selectedNoteIds.value - noteId
        } else {
            uiState.selectedNoteIds.value + noteId
        }
    }

    fun clearSelection() {
        uiState.selectedNoteIds.value = emptySet()
    }

    fun updateShowConfirmDeleteDialog(updateValue: Boolean) {
        uiState.isShowConfirmDeleteDialog.value = updateValue
    }

    fun getAllNote() {
        Log.d("isFirstLoad", isFirstLoad.toString())
        if (isFirstLoad) {
            _getListState.value = HomeState(isLoading = true)
            isFirstLoad = false
        }

        viewModelScope.launch(Dispatchers.IO) {
            noteUseCases.getUseCase.getAllNote().collect { notes ->
                if (notes.isEmpty()) {
                    _getListState.value = HomeState(error = "Can't get data from database")


                } else {
                    _listNote.value = notes
                    _getListState.value = HomeState(
                        isLoading = false,
                        isSuccess = true
                    )
                }
            }
        }
    }

    private fun searchNoteByTitle(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            noteUseCases.searchUseCase.searchNotesByTitle(title).collect { notes ->
                _listNote.value = notes
            }
        }
    }

    fun searchNoteByDate(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            noteUseCases.searchUseCase.searchNotesByDate(date).collect { notes ->
                _listNote.value = notes
            }
        }
    }

    fun deleteNotes(context: Context, onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val failedDeleteImages = mutableListOf<String>()
            val failedCancelNotifications = mutableListOf<String>()

            val noteIds = uiState.selectedNoteIds.value.toList()
            val listImg = _listNote.value
                .filter { it.id in noteIds && !it.image.isNullOrEmpty() }
                .mapNotNull { it.image }

            val deleteCount = noteUseCases.deleteUseCase.deleteNotesByIds(noteIds)

            if (deleteCount <= 0) {

                Log.d(Constants.ERROR, "HomeViewModel - deleteNotes: Delete note failed")
                return@launch
            }

            listImg.forEach { fileName ->
                try {
                    noteUseCases.deleteUseCase.deleteImage(fileName.toString())
                } catch (e: Exception) {
                    failedDeleteImages.add(fileName.toString())
                    // Log.e("DeleteImageError", "Không thể xóa ảnh cho noteId: $fileName", e)
                    Log.d(
                        Constants.ERROR,
                        "HomeViewModel - deleteNotes: Can't delete $fileName image"
                    )

                }
            }

            noteIds.forEach { noteId ->
                try {
                    noteUseCases.scheduleUseCase.cancelScheduleNotification(
                        context,
                        noteId.toString()
                    )
                } catch (e: Exception) {
                    failedCancelNotifications.add(noteId.toString())
                    Log.d(
                        Constants.ERROR,
                        "HomeViewModel - deleteNotes: Can't delete notification $noteId"
                    )

                    // Log.e("CancelNotificationError", "Không thể hủy thông báo cho noteId: $noteId", e)
                }
            }

            if (failedDeleteImages.isNotEmpty()) {
                // Log.e("Summary", "Xóa ảnh thất bại cho: $failedDeleteImages")
                Log.d(
                    Constants.ERROR,
                    "HomeViewModel - deleteNotes: delete image failed $failedDeleteImages"
                )

            }

            if (failedCancelNotifications.isNotEmpty()) {
                //  Log.e("Summary", "Hủy thông báo thất bại cho: $failedCancelNotifications")
                Log.d(
                    Constants.ERROR,
                    "HomeViewModel - deleteNotes: delete notification failed $failedCancelNotifications"
                )

            }

            withContext(Dispatchers.Main) {
              //  getAllNote() // Cập nhật lại danh sách sau khi xóa
                onSuccess()
                Log.d(Constants.STATUS, "Delete Notes Successfully")
            }

        }
    }
}
