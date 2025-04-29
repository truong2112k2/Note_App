// HomeViewModel.kt
package com.example.noteapp.presentation.home.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    val uiState = HomeUIFields()

    private val _listNote = MutableStateFlow<List<Note>>(emptyList())
    val listNote: StateFlow<List<Note>> = _listNote.asStateFlow()

    private val _getListState = MutableStateFlow(HomeState())
    val getListState: StateFlow<HomeState> = _getListState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()



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
                uiState.isDarkTheme .value  = it
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
        if(uiState.isListMode.value == false){
            clearSelection()
        }
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

    fun deleteNotes(context: Context, onSuccess: () -> Unit ) {
        viewModelScope.launch(Dispatchers.IO) {
            val failedDeleteImages = mutableListOf<String>()
            val failedCancelNotifications = mutableListOf<String>()

            val noteIds = uiState.selectedNoteIds.value.toList()
            val listImg = _listNote.value.filter { it.id in noteIds }.map { it.image }

            val deleteCount = noteUseCases.deleteUseCase.deleteNotesByIds(noteIds)

            if (deleteCount <= 0) {
                Log.e("DeleteNotes", "Xóa ghi chú thất bại.")
                return@launch
            }

            listImg.forEach { fileName ->
                try {
                    noteUseCases.deleteUseCase.deleteImage(fileName.toString())
                } catch (e: Exception) {
                    failedDeleteImages.add(fileName.toString())
                    Log.e("DeleteImageError", "Không thể xóa ảnh cho noteId: $fileName", e)
                }
            }

            noteIds.forEach { noteId ->
                try {
                    noteUseCases.scheduleUseCase.cancelScheduleNotification(context, noteId.toString())
                } catch (e: Exception) {
                    failedCancelNotifications.add(noteId.toString())
                    Log.e("CancelNotificationError", "Không thể hủy thông báo cho noteId: $noteId", e)
                }
            }

            if (failedDeleteImages.isNotEmpty()) {
                Log.e("Summary", "Xóa ảnh thất bại cho: $failedDeleteImages")
            }

            if (failedCancelNotifications.isNotEmpty()) {
                Log.e("Summary", "Hủy thông báo thất bại cho: $failedCancelNotifications")
            }

            withContext(Dispatchers.Main){
                onSuccess()
            }

        }
    }
}





































//package com.example.noteapp.presentation.home
//
//import android.content.Context
//import android.util.Log
//import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
//import androidx.compose.runtime.State
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.noteapp.domain.model.Note
//import com.example.noteapp.domain.use_case.DeleteUseCase
//import com.example.noteapp.domain.use_case.GetUseCase
//import com.example.noteapp.domain.use_case.NoteUseCases
//import com.example.noteapp.domain.use_case.ScheduleUseCase
//import com.example.noteapp.domain.use_case.SearchUseCase
//import com.example.noteapp.domain.use_case.UpdateUseCase
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.FlowPreview
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.debounce
//import kotlinx.coroutines.flow.distinctUntilChanged
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@OptIn(FlowPreview::class)
//@HiltViewModel
//class HomeViewModel @Inject constructor(
//    private val noteUseCases: NoteUseCases
//
//
//) : ViewModel() {
//
//
//
//    val showSearchView = mutableStateOf(false)
//    val searchText = mutableStateOf("")
//
//    fun updateSearchText(value: String) {
//        searchText.value = value
//
//    }
//
//    val isShowDatePicker = mutableStateOf(false)
//
//
//    private val _listNote = MutableStateFlow<List<Note>>(emptyList())
//    val listNote: StateFlow<List<Note>> = _listNote.asStateFlow()
//
//    val isDarkTheme = mutableStateOf(false)
//    val isListMode = mutableStateOf(false)
//
//    private val _getListState = mutableStateOf(HomeState())
//    val getListState: State<HomeState> = _getListState
//
//
//    fun updateShowDatePicker(updateValue: Boolean) {
//
//        isShowDatePicker.value = updateValue
//
//    }
//
//
//    fun toggleSearchView() {
//        showSearchView.value = !showSearchView.value
//    }
//
//    fun toggleTheme() {
//        isDarkTheme.value = !isDarkTheme.value
//    }
//
//    fun toggleListMode() {
//        isListMode.value = !isListMode.value
//    }
//
//
//    private var isFirstLoad = true
//    fun getAllNote() {
//
//        Log.d("isFirstLoad", isFirstLoad.toString())
//        if (isFirstLoad) {
//            _getListState.value = HomeState(isLoading = true)
//            isFirstLoad = false
//        }
//
//        viewModelScope.launch(Dispatchers.IO) {
//            noteUseCases.getUseCase.getAllNote().collect { notes ->
//                if (notes.isEmpty()) {
//                    _getListState.value = HomeState(error = "Can't get data from database")
//                } else {
//                    _listNote.value = notes
//                    _getListState.value = HomeState(
//                        isLoading = false,
//                        isSuccess = true
//                    )
//                }
//            }
//        }
//    }
//
//    private val _searchQuery = MutableStateFlow("")
//    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
//
//    fun updateSearchQuery(query: String) {
//        _searchQuery.value = query
//    }
//
//    init {
//        viewModelScope.launch {
//            searchQuery
//                .debounce(400)
//                .distinctUntilChanged()
//                .collect { query ->
//                    if (query.isBlank()) {
//                        getAllNote()
//                    } else {
//                        searchNoteByTitle(query)
//                    }
//                }
//        }
//    }
//
//
//    private fun searchNoteByTitle(title: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//
//            noteUseCases.searchUseCase.searchNotesByTitle(title).collect { notes ->
//                if (notes.isEmpty()) {
//                    _listNote.value = notes
//                } else {
//                    _listNote.value = notes
//
//                }
//
//            }
//
//        }
//
//    }
//
//    fun searchNoteByDate(date: String) {
//
//        viewModelScope.launch(Dispatchers.IO) {
//            noteUseCases.searchUseCase.searchNotesByDate(date).collect { notes ->
//                if (notes.isEmpty()) {
//                    _listNote.value = notes
//                } else {
//                    _listNote.value = notes
//
//                }
//
//            }
//
//        }
//
//    }
//
//    val selectedDate = mutableStateOf("")
//
//    fun updateSelectedDate(date: String) {
//        selectedDate.value = date
//    }
//
//
//    private val _selectedNoteIds = mutableStateOf<Set<Long>>(emptySet())
//    val selectedNoteIds: State<Set<Long>> = _selectedNoteIds
//
//    // Bật/tắt chọn một note
//    fun toggleSelection(noteId: Long) {
//        _selectedNoteIds.value = if (_selectedNoteIds.value.contains(noteId)) {
//            _selectedNoteIds.value - noteId // Bỏ chọn
//        } else {
//            _selectedNoteIds.value + noteId // Chọn thêm
//        }
//    }
//
//    // Xóa hết các lựa chọn
//    fun clearSelection() {
//        _selectedNoteIds.value = emptySet()
//    }
//
//
//    private val _deleteNotesState = mutableStateOf(HomeState())
//    val deleteNotesState: State<HomeState> = _deleteNotesState
//
//
//    val isShowConfirmDeleteDialog = mutableStateOf(false)
//
//    fun updateShowConfirmDeleteDialog(updateValue: Boolean){
//        isShowConfirmDeleteDialog.value = updateValue
//
//    }
//
//
//
//
//
//    fun deleteNotes(context: Context) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val failedDeleteImages = mutableListOf<String>()
//            val failedCancelNotifications = mutableListOf<String>()
//
//            val noteIds = _selectedNoteIds.value.toList()
//            val listImg = _listNote.value.filter {
//                it.id in noteIds
//            }.map {
//                it.image
//            } // lấy ra list link duong dan image trc khi xoa  casc note
//            val deleteCount = noteUseCases.deleteUseCase.deleteNotesByIds(noteIds) // xoa cac note
//
//            if (deleteCount <= 0) {
//                Log.e("DeleteNotes", "Xóa ghi chú thất bại.")
//                _deleteNotesState.value = HomeState(error = "Delete failed")
//
//
//                return@launch
//            }
//
//
//
//            listImg.forEach { fileName  ->
//                try {
//                    noteUseCases.deleteUseCase.deleteImage(fileName.toString())
//
//                } catch (e: Exception) {
//                    failedDeleteImages.add(fileName.toString())
//
//
//                    Log.e("DeleteImageError", "Không thể xóa ảnh cho noteId: $fileName", e)
//                }
//            }
//
//            // Xử lý hủy notification
//            noteIds.forEach { noteId ->
//                try {
//                    noteUseCases.scheduleUseCase.cancelScheduleNotification(context, noteId.toString())
//                } catch (e: Exception) {
//                    failedCancelNotifications.add(noteId.toString())
//                    Log.e("CancelNotificationError", "Không thể hủy thông báo cho noteId: $noteId", e)
//                }
//            }
//
//            // Tổng kết lỗi
//            if (failedDeleteImages.isNotEmpty()) {
//                Log.e("Summary", "Xóa ảnh thất bại cho: $failedDeleteImages")
//                _deleteNotesState.value = HomeState(error = "Delete failed")
//
//
//            }
//
//            if (failedCancelNotifications.isNotEmpty()) {
//                Log.e("Summary", "Hủy thông báo thất bại cho: $failedCancelNotifications")
//                _deleteNotesState.value = HomeState(error = "Delete failed")
//
//            }
//            _deleteNotesState.value = HomeState(isSuccess = true)
//
//        }
//    }
//
//
//
//}
