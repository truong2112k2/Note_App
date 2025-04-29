package com.example.noteapp.presentation.home.viewmodel

// HomeUIState.kt

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class HomeUIFields(
    val isDarkTheme: MutableState<Boolean> = mutableStateOf(false),
    val isListMode: MutableState<Boolean> = mutableStateOf(false),
    val showSearchView: MutableState<Boolean> = mutableStateOf(false),
    val searchText: MutableState<String> = mutableStateOf(""),
    val isShowDatePicker: MutableState<Boolean> = mutableStateOf(false),
    val selectedDate: MutableState<String> = mutableStateOf(""),
    val selectedNoteIds: MutableState<Set<Long>> = mutableStateOf(emptySet()),
    val isShowConfirmDeleteDialog: MutableState<Boolean> = mutableStateOf(false)
)
