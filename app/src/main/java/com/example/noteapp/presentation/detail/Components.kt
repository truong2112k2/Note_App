package com.example.noteapp.presentation.detail

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.noteapp.R
import com.example.noteapp.common.Constants
import com.example.noteapp.domain.model.Note
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun LoadImageFromFile(
    context: Context,
    fileName: String,
    showButtonDelete: Boolean,
    showImage: Boolean,
    onClickDelete: () -> Unit
    ) {
    val file = File(File(context.filesDir, "image"), fileName)

    if(showImage){
        if (file.exists()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = rememberAsyncImagePainter(file),
                    contentDescription = null,
                    contentScale = ContentScale.Crop, // 👉 cắt ảnh vừa khung, đẹp
                    modifier = Modifier
                        .fillMaxSize()
                )
                if(showButtonDelete){
                    IconButton(
                        onClick = {
                            onClickDelete()
                        },
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
        }
    }

    }

@Composable
fun DisplayEmptyListMessage(navController: NavController) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_language), // thay bằng icon của bạn
                contentDescription = "Note Icon",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.width(12.dp)) // khoảng cách giữa image và text

            Text(
                text = "Create your first note!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary

            )
        }

        Text(
            stringResource(R.string.welcome_message),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            stringResource(R.string.note_feature_description),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            stringResource(R.string.no_notes_yet),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            stringResource(R.string.create_note_now),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            stringResource(R.string.inspiration_message),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, shape = RoundedCornerShape(16.dp)) // 👈 thêm bóng đổ ở đây
                .background(color = MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(16.dp))
                .padding(16.dp)
                .clickable {
                    navController.navigate(Constants.ADD_NOTE_ROUTE)
                },
        ) {


            Text(
                text = "Create your first note!",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.weight(1f)) // khoảng cách giữa image và text

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight, // thay bằng icon của bạn
                contentDescription = "Note Icon",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewTopAppBar(
    onBackStack: () -> Unit,
    onSwitchAppBar : () -> Unit,
    onDeleteNote: () -> Unit
) {
   // val deleteState by detailViewModel.deleteState.collectAsState()

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
                //navController.popBackStack()
                onBackStack()

            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        actions = {

            IconButton(
                onClick = {

                onDeleteNote()
                }
            ) {
                Icon(Icons.Default.Delete, contentDescription = null)
            }
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = {
              //  detailViewModel.updateSwitchTopAppBar(!switchTopAppBar)
                onSwitchAppBar()
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
    context: Context
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
                detailViewModel.updateSwitchTopAppBar(false)
            }) {
                Icon(Icons.Default.Close, contentDescription = null)
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
                            detailViewModel.updateNoteDatabase(note, context = context )
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
    modifier: Modifier
    ) {
    val onPrimary = MaterialTheme.colorScheme.onPrimary

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
            modifier = modifier,
            style = styleText,
            color = onPrimary

        )


    }
}



@Composable
fun CategoryAndPriorityMenu(detailViewModel: DetailViewModel, note: Note, click: Boolean){


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
                        if(click){
                            detailViewModel.updateCategoryMenuExpended(!categoryMenuExpanded)
                            Log.d(Constants.STATUS_TAG_DETAIL_SCREEN, "Expanded = $categoryMenuExpanded")
                        }

                    },

            ) {
                Text(
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
                        if(click){
                            detailViewModel.updatePriorityMenuExpanded(!priorityMenuExpanded)

                        }
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


@Composable
fun DateTimeRow(
    note: Note
) {

    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    Text(text = "Last update: ${note.dateAdd}" ,
        color = onPrimaryColor,
        modifier = Modifier.fillMaxWidth(),
       style =  MaterialTheme.typography.headlineSmall.copy(
            fontSize = 18.sp

        )
    )
    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Date section (Left)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Date Icon",
                tint = onPrimaryColor

            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = note.dateNotify,
                color = onPrimaryColor,
                style =  MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 24.sp

                )
            )
        }

        // Time section (Right)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Schedule , // fix laij sau
                contentDescription = "Time Icon",
                tint = onPrimaryColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = note.timeNotify, color = onPrimaryColor,
                style =  MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 24.sp

                )
                )
        }
    }
}


