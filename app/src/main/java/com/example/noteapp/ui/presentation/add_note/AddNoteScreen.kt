package com.example.noteapp.ui.presentation.add_note

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.noteapp.common.Constants
import com.example.noteapp.ui.background.GradientBackground
import com.example.noteapp.ui.presentation.CustomDatePicker
import com.example.noteapp.ui.presentation.CustomTimePicker

@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    context: Context,
    navController: NavController,
    addNoteViewModel: AddNoteViewModel = hiltViewModel()
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        GradientBackground()
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {


            val titleNote by addNoteViewModel.titleNote
            val contentNote by addNoteViewModel.contentNote

            val listCategory = addNoteViewModel.listCategory
            val selectCategory by addNoteViewModel.selectedCategory
            val categoryMenuExpanded by addNoteViewModel.categoryMenuExpanded

            val listPriority = addNoteViewModel.listPriority
            val priorityMenuExpanded by addNoteViewModel.priorityMenuExpanded
            val selectPriority by addNoteViewModel.selectedPriority


            val showTimePicker by addNoteViewModel.showPickerTime
            val selectedTime by addNoteViewModel.selectedTime
            val selectedDate by addNoteViewModel.selectedDate


            val selectedImageUri by addNoteViewModel.selectedImageUri

            val addNoteState = addNoteViewModel.addNoteState.value
            val showDialog by addNoteViewModel.showDialog
            val dialogMessage by addNoteViewModel.dialogMessage


            val permissionGranted = remember { mutableStateOf(false) }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    permissionGranted.value = true
                    addNoteViewModel.insertNote(context)
                } else {
                    Toast.makeText(context, "Notification permission denied", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            // LAUNCHER
            val launcherPickImage = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->

                uri?.let {
                    addNoteViewModel.updateSelectedImageUri(it)
                }
            }


            // TOP_APP_BAR
            CenterTopAppBar(
                navController,
                onPrimaryColor,
                onPickImage = {launcherPickImage.launch("image/*")},
                onPickDate = {addNoteViewModel.updateShowPickerDate(true) },
                onPickTime = {addNoteViewModel.updateShowPickerTime(true)},
                onInsertNote = {addNoteViewModel.insertNote(context)},
                onRequestPermission = {launcher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                context
            )
            if (selectedTime != "00:00" || selectedDate != "00/00/0000") {
                NotificationRow(selectedDate, selectedTime, Icons.Default.Notifications)
            }

            Spacer(Modifier.height(8.dp))


            /// MENU_CATEGORY
            Row(
                modifier = Modifier.fillMaxWidth(),

                ) {
                Box(

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .width(150.dp)
                            .border(1.dp, onPrimaryColor, CircleShape)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .clickable {
                                addNoteViewModel.updateCategoryMenuExpended(!categoryMenuExpanded)
                                Log.d(Constants.STATUS_TAG_ADD_NOTE_SCREEN, "Expanded = $categoryMenuExpanded")
                            }
                    ) {
                        Text(
                            text = selectCategory.nameOrId,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f),
                            color = onPrimaryColor
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            painter = painterResource(selectCategory.icon!!), // or any icon you want
                            contentDescription = "Dropdown Icon",
                            modifier = Modifier.size(20.dp),
                            tint = onPrimaryColor
                        )
                    }
                    DropdownMenu(
                        expanded = categoryMenuExpanded,
                        onDismissRequest = { addNoteViewModel.updateCategoryMenuExpended(false) },
                        offset = DpOffset(x = 0.dp, y = 10.dp), //


                    ) {
                        listCategory.forEach { category ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = category.nameOrId,
                                        fontSize = 16.sp,
                                        color = primaryColor,

                                        )
                                },
                                onClick = {
                                    addNoteViewModel.updateSelectCategory(category)
                                    addNoteViewModel.updateCategoryMenuExpended(!categoryMenuExpanded)
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = category.icon!!),
                                        contentDescription = "",
                                        tint = primaryColor,
                                        modifier = Modifier.size(24.dp)
                                    )
                                },
                            )

                        }

                    }

                }

                Spacer(Modifier.weight(1f))

                /// MENU_PRIORITY

                Box(

                ) {

                    Text(
                        selectPriority.nameOrId,
                        modifier = Modifier
                            .width(150.dp)
                            .background(selectPriority.color!!, CircleShape)
                            .border(1.dp, onPrimaryColor, CircleShape)
                            .padding(8.dp)
                            .clickable {
                                addNoteViewModel.updatePriorityMenuExpanded(!priorityMenuExpanded)
                            },
                        textAlign = TextAlign.Center,
                        color = onPrimaryColor

                    )

                    DropdownMenu(
                        expanded = priorityMenuExpanded,
                        onDismissRequest = { addNoteViewModel.updatePriorityMenuExpanded(false) },
                        offset = DpOffset(x = 0.dp, y = 10.dp) // 👈 Lệch xuống 10.dp

                    ) {
                        listPriority.forEach { priority ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = priority.nameOrId,
                                        modifier = Modifier
                                            .background(priority.color!!)
                                            .fillMaxSize(),
                                        textAlign = TextAlign.Center
                                    )


                                },
                                onClick = {
                                    addNoteViewModel.updateSelectPriority(priority)
                                    addNoteViewModel.updatePriorityMenuExpanded(!priorityMenuExpanded)
                                },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(priority.color!!),

                                )

                        }

                    }

                }
            }

            Spacer(Modifier.height(8.dp))
            //  TEXT_FIELD_TITTLE
            OutlinedTextField(
                value = titleNote,
                onValueChange = { addNoteViewModel.updateTitleNote(it) },
                placeholder = {
                    Text(
                        text = "Title",
                        style = MaterialTheme.typography.displayLarge,
                        color = onPrimaryColor
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent,
                    cursorColor = onPrimaryColor // 👈 Màu con trỏ

                ),

                textStyle = TextStyle(fontSize = 40.sp, color = onPrimaryColor),

                )
            Spacer(Modifier.height(8.dp))


            // SHOW_IMAGE_PICK_FROM_GALLERY
            if (selectedImageUri != null) {
                Box(
                    modifier = Modifier
                        .height(280.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .matchParentSize()
                    )

                    IconButton(
                        onClick = { addNoteViewModel.updateSelectedImageUri(null) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.5f), shape = CircleShape)
                            .size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove image",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }


            // TEXT_CONTENT

            val lineHeight = 24.sp

            BasicTextField(
                value = contentNote,
                onValueChange = { addNoteViewModel.updateContentNote(it) },
                modifier = Modifier
                    .fillMaxSize()
                    .heightIn(150.dp)
                    .padding(8.dp),
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    lineHeight = lineHeight,
                    color = MaterialTheme.colorScheme.onPrimary
                ),
                cursorBrush = SolidColor(onPrimaryColor),

                decorationBox = { innerTextField ->
                    Box {

                        Canvas(modifier = Modifier.matchParentSize()) {
                            val lineSpacing = lineHeight.toPx()
                            var currentY = 0f
                            while (currentY < size.height) {
                                drawLine(
                                    color = onPrimaryColor,
                                    start = Offset(0f, currentY + lineSpacing),
                                    end = Offset(size.width, currentY + lineSpacing),
                                    strokeWidth = 1f
                                )
                                currentY += lineSpacing
                            }
                        }


                        if (contentNote.isEmpty()) {
                            Text(
                                text = "Content",
                                lineHeight = lineHeight,
                                style = MaterialTheme.typography.titleLarge,
                                color = onPrimaryColor
                            )

                        }
                        innerTextField() // TextField content
                    }
                }
            )

            // PICK_KER_TIME

            if (showTimePicker) {
                CustomTimePicker(
                    onConfirm = { timeState ->
                        val hour = timeState.hour
                        val minute = timeState.minute
                        addNoteViewModel.updateShowPickerTime(false)
                        addNoteViewModel.updateSelectedTime(
                            String.format(
                                "%02d:%02d",
                                hour,
                                minute
                            )
                        )
                    },
                    onDismiss = {
                        addNoteViewModel.updateShowPickerTime(false)

                    }
                )
            }
            // PICK_KER_DATE


            val showPickerDate by addNoteViewModel.showPickerDate
            CustomDatePicker(
                showPickerDate = showPickerDate,
                onClickPositiveButton = {addNoteViewModel.updateShowPickerDate(false)},
               onClickNegativeButton = {addNoteViewModel.updateShowPickerDate(false)},
                onSelectDate = { addNoteViewModel.updateSelectedDate(it)})

            LaunchedEffect(addNoteState.isSuccess, addNoteState.error) {
                if (addNoteState.isSuccess) {
                    addNoteViewModel.updateDialogMessage("Note added successfully!")
                    addNoteViewModel.updateShowDialog(true)
                } else if (addNoteState.error.isNotBlank()) {
                    addNoteViewModel.updateDialogMessage(addNoteState.error)
                    addNoteViewModel.updateShowDialog(true)
                }
            }
// ALERTDIALOG_NOTIFICATION
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        addNoteViewModel.updateShowDialog(false)
                        addNoteViewModel.resetAddState()
                    },
                    title = {
                        Text(text = if (addNoteState.isSuccess) "Success" else "Error")
                    },
                    text = {
                        Text(dialogMessage)
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                addNoteViewModel.updateShowDialog(false)
                                addNoteViewModel.resetAddState()
                                if (addNoteState.isSuccess) {
                                    addNoteViewModel.resetAddNoteField()
                                    navController.popBackStack()
                                }
                            }
                        ) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}

