package com.example.noteapp.ui.presentation.add_note

 data class AddNoteState(
     val isLoading: Boolean = false,
     var isSuccess: Boolean = false,
     var error: String = ""
 )