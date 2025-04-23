package com.example.noteapp.presentation.add_note

 data class AddNoteState(
     val isLoading: Boolean = false,
     var isSuccess: Boolean = false,
     var error: String = ""
 )