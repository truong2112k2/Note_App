package com.example.noteapp.presentation.detail.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import com.example.noteapp.R
import com.example.noteapp.domain.model.ItemDropMenu
import com.example.noteapp.ui.theme.high
import com.example.noteapp.ui.theme.low
import com.example.noteapp.ui.theme.medium

class DetailScreenFields {

    val isShowAllDetail = mutableStateOf(false)

    val switchTopAppBar = mutableStateOf(false)

    val showDiaLogDelete = mutableStateOf(false)

    val listCategory = listOf(
        ItemDropMenu("Work", icon = R.drawable.ic_work),
        ItemDropMenu("Relax", icon = R.drawable.ic_relax),
        ItemDropMenu("Sport", icon = R.drawable.ic_sport),
        ItemDropMenu("Language", icon = R.drawable.ic_language),
        ItemDropMenu("Else", icon = R.drawable.ic_else),
    )

    val listPriority = listOf(
        ItemDropMenu("1", color = low),
        ItemDropMenu("2", color = medium),
        ItemDropMenu("3", color = high),
    )



    var categoryMenuExpanded = mutableStateOf(false)
    var priorityMenuExpanded = mutableStateOf(false)
    var showPickerTime = mutableStateOf(false)
    var showPickerDate = mutableStateOf(false)
    var selectedImageUri = mutableStateOf<Uri?>(null) // reset
    var showDialogUpdate = mutableStateOf(false)
    var dialogUpdateMessage = mutableStateOf("") //reset
    val showImage = mutableStateOf(true)


}