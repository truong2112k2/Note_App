package com.example.noteapp.presentation.add_note.viewmodel

 data class AddNoteState(
     val isLoading: Boolean = false,
     var isSuccess: Boolean = false,
     var error: String = ""
 )