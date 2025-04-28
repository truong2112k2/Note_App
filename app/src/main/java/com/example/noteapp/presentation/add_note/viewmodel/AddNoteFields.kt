package com.example.noteapp.presentation.add_note.viewmodel
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.example.noteapp.R
import com.example.noteapp.domain.model.ItemDropMenu
import com.example.noteapp.ui.theme.high
import com.example.noteapp.ui.theme.low
import com.example.noteapp.ui.theme.medium

class AddNoteFields {

    val titleNote = mutableStateOf("")
    val contentNote = mutableStateOf("")

    private val defaultItemCategory = ItemDropMenu("Category", icon = R.drawable.ic_category)
    val listCategory = listOf(
        ItemDropMenu("Work", icon = R.drawable.ic_work),
        ItemDropMenu("Relax", icon = R.drawable.ic_relax),
        ItemDropMenu("Sport", icon = R.drawable.ic_sport),
        ItemDropMenu("Language", icon = R.drawable.ic_language),
        ItemDropMenu("Else", icon = R.drawable.ic_else),
    )
    var categoryMenuExpanded = mutableStateOf(false)
    var selectedCategory = mutableStateOf(defaultItemCategory)

    val defaultItemPriority = ItemDropMenu("Priority", color = Color.Transparent)
    val listPriority = listOf(
        ItemDropMenu("1", color = low),
        ItemDropMenu("2", color = medium),
        ItemDropMenu("3", color = high),
    )
    var priorityMenuExpanded = mutableStateOf(false)
    var selectedPriority = mutableStateOf(defaultItemPriority)

    var showPickerTime = mutableStateOf(false)
    var selectedTime = mutableStateOf("00:00")

    var showPickerDate = mutableStateOf(false)
    var selectedDate = mutableStateOf("00/00/0000")

    var selectedImageUri = mutableStateOf<Uri?>(null)

    var showDialog = mutableStateOf(false)
    var dialogMessage = mutableStateOf("")

    fun resetFields() {
        titleNote.value = ""
        contentNote.value = ""
        selectedCategory.value = defaultItemCategory
        selectedPriority.value = defaultItemPriority
        selectedTime.value = "00:00"
        selectedDate.value = "00/00/0000"
        selectedImageUri.value = null
        dialogMessage.value = ""
    }
}
