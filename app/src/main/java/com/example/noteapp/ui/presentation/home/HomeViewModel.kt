package com.example.noteapp.ui.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.use_case.GetNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNotesUseCase: GetNoteUseCase
) : ViewModel() {

    val searchText = mutableStateOf("")
    fun updateSearchText(value: String) {
        searchText.value = value
    }




    private val _listNote = MutableStateFlow<List<Note>>(emptyList())
    val listNote: StateFlow<List<Note>> = _listNote.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    private val _homeState = mutableStateOf(HomeState())
    val homeState : State<HomeState> = _homeState

/*
    private val _addNoteState = mutableStateOf(AddNoteState())
    val addNoteState: State<AddNoteState> = _addNoteState
 */
    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    // Sử dụng Map thay vì List để gán theo note.id
    private val _colors = mutableStateMapOf<Int, Color>()
    val colors: Map<Int, Color> get() = _colors

    private val _heights = mutableStateMapOf<Int, Dp>()
    val heights: Map<Int, Dp> get() = _heights

    fun getAllNote() {

        _homeState.value = HomeState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            val notes = getNotesUseCase.getAllNote()


            if( notes.isEmpty()){
                _homeState.value = HomeState(error = "Can't get data from database")
            }else{

                _listNote.value = notes
                generateRandomStyles(notes)

                _homeState.value = HomeState(
                    isLoading = false,
                    isSuccess = true
                )


            }
        }
    }

    private fun generateRandomStyles(notes: List<Note>) {
        for (note in notes) {
            if (!_colors.containsKey(note.id.toInt())) {
                _colors[note.id.toInt()] = generateReadableLightColor()
            }
            if (!_heights.containsKey(note.id.toInt())) {
                _heights[note.id.toInt()] = (20..100).random().dp
            }
        }
    }

    private fun generateReadableLightColor(): Color {
        while (true) {
            val red = (100..255).random()
            val green = (100..255).random()
            val blue = (100..255).random()

            val luminance = (0.299 * red + 0.587 * green + 0.114 * blue) / 255

            // Đảm bảo nền đủ sáng để chữ đen nhìn rõ
            if (luminance > 0.7) {
                return Color(
                    red / 255f,
                    green / 255f,
                    blue / 255f,
                    1f
                )
            }
        }
    }
}


