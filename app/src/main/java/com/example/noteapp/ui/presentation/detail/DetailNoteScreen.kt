package com.example.noteapp.ui.presentation.detail

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.noteapp.R
import com.example.noteapp.common.Constants
import com.example.noteapp.domain.model.Note
import com.example.noteapp.ui.background.GradientBackground
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun DetailNoteScreen(idNote: String, navController: NavController, detailViewModel: DetailViewModel = hiltViewModel()) {



    var visible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val showTimePicker by detailViewModel.showPickerTime
    val note by detailViewModel.note.collectAsState()

    val switchTopAppBar by detailViewModel.switchTopAppBar
    val selectedImage by detailViewModel.selectedImageUri
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
            .statusBarsPadding()
            .alpha(alpha)
    ) {
        GradientBackground()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {






            AnimatedContent(
                targetState = switchTopAppBar,
                transitionSpec = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth }, // từ phải
                    ) + fadeIn() with
                            slideOutHorizontally(
                                targetOffsetX = { fullWidth -> -fullWidth }, // ra trái
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
                        navController

                    )
                } else {
                    ViewTopAppBar(
                        detailViewModel = detailViewModel,
                        switchTopAppBar = switchTopAppBar,
                        navController = navController
                    )
                }
            }


            ShowDateTimeNotify()


                EditableTextContent(
                    text = note.title,
                    onTextChange = {
                        detailViewModel.updateNote(
                            note.copy(
                                title = it
                            )
                        )
                    },
                    switchTopAppBar,
                   styleTextField =  MaterialTheme.typography.headlineLarge,
                   styleText =    MaterialTheme.typography.displayMedium
                )

            Spacer(Modifier.height(16.dp))


            if(note.image != null && selectedImage == null ){

              LoadImageFromFile(context, note.image.toString())
                Log.d(Constants.STATUS_TAG_DETAIL_SCREEN,"Image name value in screen = ${note.image.toString()}")


            }else if(selectedImage != null ){
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
                        modifier = Modifier
                            .matchParentSize()
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
            else{
                Text("k co anh?")
            }

            Spacer(Modifier.height(16.dp))

            MenuCategoryAndPriority(detailViewModel,note)

            Spacer(Modifier.height(16.dp))

            EditableTextContent(
                note.content,
                onTextChange = {
                    detailViewModel.updateNote( // update content
                        note.copy(
                            content = it
                        )
                    )
                },
                switchTopAppBar,
                MaterialTheme.typography.headlineSmall,
                MaterialTheme.typography.headlineSmall,

            )





            if (showTimePicker) {
                CustomTimePicker(
                    onConfirm = { timeState ->
                        val hour = timeState.hour
                        val minute = timeState.minute
                        detailViewModel.updateShowPickerTime(false)

                        detailViewModel.updateNote(note.copy(
                            timeNotify = String.format("%02d:%02d", hour, minute)
                        ))
                    },
                    onDismiss = {
                        detailViewModel.updateShowPickerTime(false)

                    }
                )
            }

            ComposeDatePicker(detailViewModel, note)

        }


    }


    val detailState by detailViewModel.detailState.collectAsState()
    val showDialog by detailViewModel.showDialog
    val dialogMessage by detailViewModel.dialogMessage
    LaunchedEffect(detailState.isSuccess, detailState.error) {
        if (detailState.isSuccess) {
            detailViewModel.updateDialogMessage("Note update successfully!")
            detailViewModel.updateShowDialog(true)
        } else if (detailState.error.isNotBlank()) {
            detailViewModel.updateDialogMessage(detailState.error)
            detailViewModel.updateShowDialog(true)
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                detailViewModel.updateShowDialog(false)

            },
            title = {
                Text(text = if (detailState.isSuccess) "Success" else "Error")
            },
            text = {
                Text(dialogMessage)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        detailViewModel.updateShowDialog(false)
                        if (detailState.isSuccess) {
                            detailViewModel.updateSwitchTopAppBar(false)
                        }
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }


}

@Composable
fun ShowDateTimeNotify() {


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewTopAppBar(detailViewModel: DetailViewModel, switchTopAppBar: Boolean, navController: NavController) {

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
            Text(
                "Detail Note",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                   navController.popBackStack()

            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = {
                detailViewModel.updateSwitchTopAppBar(!switchTopAppBar)
            }) {
                Icon(Icons.Default.Refresh, contentDescription = null)
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTopAppBar(
    detailViewModel: DetailViewModel,
    switchTopAppBar: Boolean,
    launcherPickImage: ManagedActivityResultLauncher<String, Uri?>,
    note: Note,
    navController: NavController
) {

    val selectImage by detailViewModel.selectedImageUri
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround, // khoảng cách đều nhau
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.height(10.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)

                        .clickable {
                        //    Log.d("CheckValueNote", note.toString())
                            Log.d(Constants.STATUS_TAG_DETAIL_SCREEN, "ImageSelect = ${selectImage} Note = ${note.toString()}" )
                            launcherPickImage.launch("image/*")



                        }
                )
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {

                            detailViewModel.updateShowPickerDate(true )


                        }
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_time),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            detailViewModel.updateShowPickerTime(true )

                        }
                )
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {

                            detailViewModel.updateNoteDatabase(note)
                             Log.d(Constants.STATUS_TAG_DETAIL_SCREEN,"Update Note Success In Screen Detail")
                            detailViewModel.updateSwitchTopAppBar(!switchTopAppBar)
                        }
                )
            }
        }

    )
}


@Composable
fun EditableTextContent(
    text: String,
    onTextChange: (String) -> Unit,
    isEditing: Boolean,
    styleTextField: TextStyle,
    styleText: TextStyle,

    ) {



    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val colorPrimary = MaterialTheme.colorScheme.primary
    if (isEditing) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = styleTextField,
            colors = TextFieldDefaults.colors(
                focusedTextColor = onPrimary,
                unfocusedTextColor = onPrimary,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor =  Color.Transparent,
                focusedIndicatorColor = onPrimary,
                unfocusedIndicatorColor = onPrimary,
                focusedPlaceholderColor = onPrimary,
                unfocusedPlaceholderColor = onPrimary,
                cursorColor = onPrimary,
                errorCursorColor = onPrimary,
            ),






            )
    } else {


        Text(
            text = text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.Transparent)
            ,
            style = styleText,
            maxLines = 2 ,
            overflow = TextOverflow.Ellipsis,
            color = onPrimary


        )
    }
}

@Composable
fun MenuCategoryAndPriority(detailViewModel: DetailViewModel, note: Note){


    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val listCategory = detailViewModel.listCategory
    val categoryMenuExpanded by detailViewModel.categoryMenuExpanded
    val selectCategory by detailViewModel.selectedCategory


    val listPriority = detailViewModel.listPriority
    val selectPriority by detailViewModel.selectedPriority
    val priorityMenuExpanded by detailViewModel.priorityMenuExpanded



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
                        detailViewModel.updateCategoryMenuExpended(!categoryMenuExpanded)
                        Log.d(Constants.STATUS_TAG_DETAIL_SCREEN, "Expanded = $categoryMenuExpanded")
                    }
            ) {
                Text(
                  //  text = selectCategory.nameOrId,
                    text = note.category,
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
                onDismissRequest = { detailViewModel.updateCategoryMenuExpended(false) },
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
                            detailViewModel.updateNote(
                                note.copy(
                                    category = category.nameOrId
                                )
                            )
                            detailViewModel.updateCategoryMenuExpended(!categoryMenuExpanded)
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
                note.priority.toString(),
                modifier = Modifier
                    .width(150.dp)
                    .background(selectPriority.color!!, CircleShape)
                    .border(1.dp, onPrimaryColor, CircleShape)
                    .padding(8.dp)
                    .clickable {
                        detailViewModel.updatePriorityMenuExpanded(!priorityMenuExpanded)
                    },
                textAlign = TextAlign.Center,
                color = onPrimaryColor

            )

            DropdownMenu(
                expanded = priorityMenuExpanded,
                onDismissRequest = { detailViewModel.updatePriorityMenuExpanded(false) },
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
                          //  detailViewModel.updateSelectPriority(priority)
                            detailViewModel.updateNote(
                                note.copy(
                                    priority = priority.nameOrId.toInt()
                                )
                            )
                            detailViewModel.updatePriorityMenuExpanded(!priorityMenuExpanded)
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(priority.color!!),

                        )

                }

            }

        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePicker(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    AlertDialogTimePicker(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState) }
    ) {
        TimePicker(
            state = timePickerState,
        )
    }
}

@Composable
fun AlertDialogTimePicker(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
            }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ComposeDatePicker(detailViewModel: DetailViewModel, note: Note) {


    val showPickerDate by detailViewModel.showPickerDate
    val dateDialogState = rememberMaterialDialogState()

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    if(showPickerDate){
        dateDialogState.show()
    }

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton("OK"){

                detailViewModel.updateShowPickerDate(false)

            }
            negativeButton("Cancel"){
                detailViewModel.updateShowPickerDate(false)

            }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Pick Date"
        ) {

            detailViewModel.updateNote(
                note.copy(
                    dateNotify = it.format(formatter)

                ))


        }
    }
}

