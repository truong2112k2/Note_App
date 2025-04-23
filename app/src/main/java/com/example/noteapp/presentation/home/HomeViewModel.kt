package com.example.noteapp.presentation.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LOG_TAG
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.use_case.GetNoteUseCase
import com.example.noteapp.domain.use_case.SearchNoteUseCase
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
    private val searchNoteUseCase: SearchNoteUseCase
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




    val resultSearch = mutableStateOf("")


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


}
