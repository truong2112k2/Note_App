package com.example.noteapp.presentation.detail

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.noteapp.presentation.ConfirmDeleteDialog
import com.example.noteapp.presentation.CustomDatePicker
import com.example.noteapp.presentation.CustomTimePicker
import com.example.noteapp.presentation.home.ShowConfirmDeleteDialog
import com.example.noteapp.ui.background.GradientBackground
import java.time.format.DateTimeFormatter

@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun DetailNoteScreen(
    idNote: String,
    navController: NavController,
    detailViewModel: DetailViewModel = hiltViewModel()
) {


    var visible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val showTimePicker by detailViewModel.showPickerTime
    val note by detailViewModel.note.collectAsState()

    val updateState by detailViewModel.updateState.collectAsState()
    val showDialogUpdate by detailViewModel.showDialogUpdate
    val dialogUpdateMessage by detailViewModel.dialogUpdateMessage


    val switchTopAppBar by detailViewModel.switchTopAppBar
    val selectedImage by detailViewModel.selectedImageUri

    val showDialogDelete by detailViewModel.showDiaLogDelete
    // LAUNCHER
    val launcherPickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->

        uri?.let {
            detailViewModel.updateSelectedImageUri(it)
        }
    }


    LaunchedEffect(Unit) {
        detailViewModel.getNoteById(idNote.toLong())
        visible = true

    }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000), // 1s fade-in
        label = "fadeInAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(alpha)
    ) {
        GradientBackground()


        Column(

        ) {
            AnimatedContent(
                targetState = switchTopAppBar,
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                    ) + fadeIn() with
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> -fullWidth },
                            ) + fadeOut()
                },
                label = "TopAppBarSwitch"
            ) { targetState ->
                if (targetState) {
                    EditTopAppBar(
                        detailViewModel = detailViewModel,
                        switchTopAppBar = switchTopAppBar,
                        launcherPickImage,
                        note,
                        context = context
                    )
                } else {
                    ViewTopAppBar(
                        onBackStack = {
                            navController.popBackStack()
                        },
                        onSwitchAppBar = {
                            detailViewModel.updateSwitchTopAppBar(!switchTopAppBar)
                        },
                        onDeleteNote = {


                            detailViewModel.setShowDialogDelete(true)


                        },
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                Spacer(Modifier.height(4.dp))

                DateTimeRow(note)

                Spacer(Modifier.height(16.dp))

                EditableTextContent(
                    text = note.title,
                    onTextChange = {
                        detailViewModel.updateNote(
                            note.copy(
                                title = it
                            )
                        )
                    },
                    isEditing = switchTopAppBar,
                    styleTextField = MaterialTheme.typography.headlineLarge,
                    styleText = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                )


                val showImage by detailViewModel.showImage
                Spacer(Modifier.height(16.dp))

                if (note.image != null && selectedImage == null) {
                    LoadImageFromFile(context, note.image.toString(), switchTopAppBar, showImage,
                        onClickDelete = { detailViewModel.updateShowImage(false) }
                    )
                } else if (selectedImage != null) {
                    Box(
                        modifier = Modifier
                            .height(280.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(selectedImage),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )

                        IconButton(
                            onClick = { detailViewModel.updateSelectedImageUri(null) },
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

                Spacer(Modifier.height(16.dp))

                CategoryAndPriorityMenu(detailViewModel, note, switchTopAppBar)

                Spacer(Modifier.height(16.dp))


                EditableTextContent(
                    text = note.content,
                    onTextChange = {
                        detailViewModel.updateNote(
                            note.copy(
                                content = it
                            )
                        )
                    },
                    isEditing = switchTopAppBar,
                    styleTextField = MaterialTheme.typography.headlineSmall,
                    styleText = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 21.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                )

                if (showTimePicker) {
                    CustomTimePicker(
                        onConfirm = { timeState ->
                            val hour = timeState.hour
                            val minute = timeState.minute
                            detailViewModel.updateShowPickerTime(false)

                            detailViewModel.updateNote(
                                note.copy(
                                    timeNotify = String.format("%02d:%02d", hour, minute)
                                )
                            )
                        },
                        onDismiss = {
                            detailViewModel.updateShowPickerTime(false)
                        }
                    )
                }

                val showDatePicker by detailViewModel.showPickerDate
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

                CustomDatePicker(
                    showDatePicker,
                    onClickPositiveButton = { detailViewModel.updateShowPickerDate(false) },
                    onClickNegativeButton = { detailViewModel.updateShowPickerDate(false) },
                    onSelectDate = {
                        detailViewModel.updateNote(
                            note.copy(
                                dateNotify = it.format(formatter)
                            )
                        )
                    }
                )
            }


        }


    }



    LaunchedEffect(updateState) {
        if (updateState.isSuccess) {
            detailViewModel.setDialogUpdateMessage("Note update successfully!")
            detailViewModel.setShowDialogUpdate(true)
        } else if (updateState.error.isNotBlank()) {
            detailViewModel.setDialogUpdateMessage(updateState.error)
            detailViewModel.setShowDialogUpdate(true)
        }
    }

    if (showDialogUpdate) {
        AlertDialog(
            onDismissRequest = {
                detailViewModel.setShowDialogUpdate(false)

            },
            title = {
                Text(text = if (updateState.isSuccess) "Success" else "Error")
            },
            text = {
                Text(dialogUpdateMessage)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        detailViewModel.setShowDialogUpdate(false)
                        if (updateState.isSuccess) {
                            detailViewModel.updateSwitchTopAppBar(false)
                        }
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    if (showDialogDelete) {
        ConfirmDeleteDialog(
            context = context,
            title = "Notification",
            message = "Are you sure about delete this note?",
            positiveText = "Ok",
            negativeText = "Cancel",
            onConfirm = {
                detailViewModel.deleteNoteById(context, note, onSuccess = {
                    navController.popBackStack()

                }
                )
            },
            onCancel = {
                detailViewModel.setShowDialogDelete(false)
            }
        )
    }

}

