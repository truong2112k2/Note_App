package com.example.noteapp.presentation.add_note

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
import androidx.compose.runtime.setValue
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
import com.example.noteapp.presentation.CustomDatePicker
import com.example.noteapp.presentation.CustomTimePicker
import com.example.noteapp.presentation.add_note.viewmodel.AddNoteViewModel

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
            val selectedTime by addNoteViewModel.selectedTime
            val selectedDate by addNoteViewModel.selectedDate
            val selectedImageUri by addNoteViewModel.selectedImageUri

            val categoryMenuExpanded by addNoteViewModel.categoryMenuExpanded
            val listCategory = addNoteViewModel.listCategory
            val selectedCategory by addNoteViewModel.selectedCategory

            val priorityMenuExpanded by addNoteViewModel.priorityMenuExpanded
            val listPriority = addNoteViewModel.listPriority
            val selectedPriority by addNoteViewModel.selectedPriority

            val showTimePicker by addNoteViewModel.showPickerTime
            val showDialog by addNoteViewModel.showDialog
            val dialogMessage by addNoteViewModel.dialogMessage
            val addNoteState by addNoteViewModel.addNoteState


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
                            .clip(CircleShape)
                            .border(1.dp, onPrimaryColor, CircleShape)
                            .clickable { addNoteViewModel.updateCategoryMenuExpanded(!categoryMenuExpanded) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = selectedCategory.nameOrId,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f),
                            color = onPrimaryColor
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            painter = painterResource(selectedCategory.icon!!), // or any icon you want
                            contentDescription = "Dropdown Icon",
                            modifier = Modifier.size(20.dp),
                            tint = onPrimaryColor
                        )
                    }
                    DropdownMenu(
                        expanded = categoryMenuExpanded,
                        onDismissRequest = { addNoteViewModel.updateCategoryMenuExpanded(isExpanded = false) },
                        offset = DpOffset(x = 0.dp, y = 10.dp), //
                    ) {
                        listCategory.forEach { category ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = category.nameOrId,
                                        fontSize = 16.sp,
                                        color = onPrimaryColor,
                                        )
                                },
                                onClick = {
                                    addNoteViewModel.updateSelectedCategory(category)
                                    addNoteViewModel.updateCategoryMenuExpanded(!categoryMenuExpanded)
                                },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = category.icon!!),
                                        contentDescription = "",
                                        tint = onPrimaryColor,
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
                    modifier = Modifier
                        .width(150.dp)
                        .background(selectedPriority.color!!,CircleShape)
                        .clip(CircleShape)
                        .border(1.dp, onPrimaryColor, CircleShape)
                        .clickable {
                            addNoteViewModel.updatePriorityMenuExpanded(!priorityMenuExpanded)
                        }
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        selectedPriority.nameOrId,
                        color = onPrimaryColor

                    )

                    DropdownMenu(
                        expanded = priorityMenuExpanded,
                        onDismissRequest = { addNoteViewModel.updatePriorityMenuExpanded(false) },
                        offset = DpOffset(x = 0.dp, y = 10.dp)

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
                                    addNoteViewModel.updateSelectedPriority(priority)
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
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 42.sp
                        ),
                        color = onPrimaryColor
                    )
                },
                modifier = Modifier.fillMaxWidth(),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent,
                    cursorColor = onPrimaryColor

                ),

                textStyle = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 42.sp
                ),
                maxLines = 1

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


            OutlinedTextField(
                value = contentNote,
                onValueChange = {
                    addNoteViewModel.updateContentNote(it)
                                },
                placeholder = {
                    Text(
                        text = "Enter some content",
                        style = TextStyle(fontSize = 24.sp, color = onPrimaryColor),
                        color = onPrimaryColor
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                //singleLine = false,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent,
                    cursorColor = onPrimaryColor

                ),

                textStyle = TextStyle(fontSize = 24.sp, color = onPrimaryColor),


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
                                Log.d("Check","Before add" +addNoteState.isSuccess.toString() )
                                if (addNoteState.isSuccess) {
                                    addNoteViewModel.resetFields()
                                    navController.popBackStack()
                                }
                                addNoteViewModel.resetAddState()
                                Log.d("Check","After add" + addNoteState.isSuccess.toString() )
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