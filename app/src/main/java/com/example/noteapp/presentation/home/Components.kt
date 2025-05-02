// Components.kt
package com.example.noteapp.presentation.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.noteapp.R
import com.example.noteapp.domain.model.Note
import com.example.noteapp.presentation.ConfirmDeleteDialog
import com.example.noteapp.presentation.CreateATitle
import com.example.noteapp.presentation.CustomDatePicker
import com.example.noteapp.presentation.home.viewmodel.HomeViewModel
import com.example.noteapp.ui.theme.high
import com.example.noteapp.ui.theme.low
import com.example.noteapp.ui.theme.medium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHomeScreen(
    homeViewModel: HomeViewModel
) {
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val isDarkTheme by homeViewModel.uiState.isDarkTheme
    val isShowSearchView by homeViewModel.uiState.showSearchView
    val isListMode by homeViewModel.uiState.isListMode

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            navigationIconContentColor = onPrimaryColor,
            actionIconContentColor = onPrimaryColor
        ),
        title = {},
        navigationIcon = {
            CreateATitle("Home")
        },
        actions = {
            IconButton(onClick = { homeViewModel.toggleTheme() }) {
                Icon(
                    painter = painterResource(if (isDarkTheme) R.drawable.ic_dark_theme else R.drawable.ic_light_theme),
                    contentDescription = null,
                    tint = onPrimaryColor
                )
            }

            IconButton(onClick = { homeViewModel.toggleSearchView() }) {
                Icon(
                    painter = painterResource(if (isShowSearchView) R.drawable.ic_cancel else R.drawable.ic_search),
                    contentDescription = null,
                    tint = onPrimaryColor
                )
            }

            IconButton(
                onClick = {
                    if (isListMode == false) {
                        homeViewModel.clearSelection()
                    }
                    homeViewModel.toggleListMode()
                    Log.d("Check mode List", "Click List Mode = $isListMode")
                },
                modifier = Modifier.background(
                    if (isListMode) onPrimaryColor.copy(alpha = 0.3f) else Color.Transparent,
                    CircleShape
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Checklist,
                    contentDescription = null,
                    tint = onPrimaryColor
                )
            }
        }
    )
}

@SuppressLint("NewApi", "SuspiciousIndentation")
@Composable
fun SearchBar(homeViewModel: HomeViewModel) {
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val isShowSearchView by homeViewModel.uiState.showSearchView
    val isShowDatePicker by homeViewModel.uiState.isShowDatePicker
    val selectedDate by homeViewModel.uiState.selectedDate

    AnimatedVisibility(
        visible = isShowSearchView,
        enter = fadeIn(tween(300)) + scaleIn(tween(300)) + slideInVertically(initialOffsetY = { -it / 3 }),
        exit = fadeOut(tween(300)) + scaleOut(tween(300)) + slideOutVertically(targetOffsetY = { -it / 3 })
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent, RoundedCornerShape(8.dp))
                    .border(1.dp, onPrimaryColor, RoundedCornerShape(8.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextField(
                    value = homeViewModel.uiState.searchText.value,
                    onValueChange = {
                        homeViewModel.updateSearchText(it)
                        homeViewModel.updateSearchQuery(it)
                    },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = onPrimaryColor
                    ),
                    textStyle = TextStyle(color = onPrimaryColor),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable { homeViewModel.updateShowDatePicker(true) },
                            tint = onPrimaryColor
                        )
                    },
                    singleLine = true,
                    label = {
                        Text(
                            "By name",
                            color = onPrimaryColor,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                )
            }

            if (selectedDate.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Filter by : $selectedDate",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.clickable {
                            homeViewModel.updateSelectedDate("")
                            homeViewModel.getAllNote()
                        }
                    )
                }
            }
        }
    }

    CustomDatePicker(
        isShowDatePicker,
        onClickPositiveButton = { homeViewModel.updateShowDatePicker(false) },
        onClickNegativeButton = { homeViewModel.updateShowDatePicker(false) },
        onSelectDate = {
            homeViewModel.updateSelectedDate(it)
            homeViewModel.searchNoteByDate(it)
        }
    )
}

@Composable
fun NoteItem(note: Note, onClickItem: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClickItem() },
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.65f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (note.priority == 1) low else if (note.priority == 2) medium else high)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = note.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(note.height.dp))
            Text(
                text = note.content,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun NoteItemSelected(note: Note, isSelected: Boolean, onToggleSelect: () -> Unit) {
    val cardShape = RoundedCornerShape(7.dp)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                BorderStroke(
                    1.dp,
                    if (isSelected) MaterialTheme.colorScheme.onPrimary else Color.Transparent
                ), cardShape
            ),
        shape = cardShape,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.65f)),
        onClick = { onToggleSelect() }
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onToggleSelect() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Transparent,
                        uncheckedColor = MaterialTheme.colorScheme.onPrimary,
                        checkmarkColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(note.height.dp))
            Text(
                text = note.content,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun ShowConfirmDeleteDialog(
    isDeleteDialog: Boolean? = null,
    onDeleteNote: () -> Unit,
    onCancel: () -> Unit,
    sumNotes: Int


) {
    if (isDeleteDialog == true) {

        ConfirmDeleteDialog(

            title = "Notification",
            message = "Are you sure about delete ${sumNotes} notes?",
            positiveText = "Ok",
            negativeText = "Cancel",
            onConfirm = {

                onDeleteNote()
                onCancel()


            },
            onCancel = {
                onCancel()

            }
        )


    } else {
        ConfirmDeleteDialog(

            title = "Notification",
            message = "None notes selected",
            positiveText = "Ok",
            negativeText = "Cancel",
            onConfirm = {

                onCancel()

            },
            onCancel = {
                onCancel()
            }
        )
    }
}









