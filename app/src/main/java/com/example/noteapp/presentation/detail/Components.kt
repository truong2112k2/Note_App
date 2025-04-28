package com.example.noteapp.presentation.detail

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.noteapp.R
import com.example.noteapp.common.Constants
import com.example.noteapp.domain.model.Note
import java.io.File

@Composable
fun LoadImageFromFile(
    context: Context,
    fileName: String,
    showButtonDelete: Boolean,
    showImage: Boolean,
    onClickDelete: () -> Unit
) {
    val file = File(File(context.filesDir, "image"), fileName)

    if (showImage) {
        if (file.exists()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(file),
                    contentDescription = null,
                    contentScale = ContentScale.Crop, // 👉 cắt ảnh vừa khung, đẹp
                    modifier = Modifier
                        .fillMaxSize()
                )
                if (showButtonDelete) {
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
            .clickable {
                navController.navigate(Constants.ADD_NOTE_ROUTE)
            }
            .padding(16.dp),


        ) {


        Text(
            text = "Create your first note!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,

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
    onSwitchAppBar: () -> Unit,
    onDeleteNote: () -> Unit,

    ) {

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
        },
        navigationIcon = {

            Row(

                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    //navController.popBackStack()
                    onBackStack()

                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }

                Spacer(Modifier.width(20.dp))

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
                onSwitchAppBar()
            }) {
                Icon(Icons.Default.Update, contentDescription = null)
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
    val categoryMenuExpanded by detailViewModel.categoryMenuExpanded
    val priorityMenuExpanded by detailViewModel.priorityMenuExpanded
    val isShowAllDetail by detailViewModel.isShowAllDetail

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
                if(isShowAllDetail){
                    detailViewModel.updateIsShowAllDetail(false)
                }
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

                            Log.d(
                                Constants.STATUS_TAG_DETAIL_SCREEN,
                                "ImageSelect = ${selectImage} Note = ${note.toString()}"
                            )
                            launcherPickImage.launch("image/*")


                        }
                )
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {

                            detailViewModel.updateShowPickerDate(true)


                        }
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_time),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            detailViewModel.updateShowPickerTime(true)
                        }
                )

                Icon(
                    imageVector = Icons.Default.PriorityHigh,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            detailViewModel.updatePriorityMenuExpanded(!priorityMenuExpanded)
                        }
                )


                Icon(
                    imageVector = Icons.Default.Category,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            detailViewModel.updateCategoryMenuExpended(!categoryMenuExpanded)
                        }
                )




                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            detailViewModel.updateNoteDatabase(note, context = context)
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
    isTitle : Boolean
) {
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    if (isEditing) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier
                .fillMaxWidth(),
            textStyle =MaterialTheme.typography.headlineSmall,
            colors = TextFieldDefaults.colors(
                focusedTextColor = onPrimary,
                unfocusedTextColor = onPrimary,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = onPrimary,
                unfocusedIndicatorColor = onPrimary,
                focusedPlaceholderColor = onPrimary,
                unfocusedPlaceholderColor = onPrimary,
                cursorColor = onPrimary,
                errorCursorColor = onPrimary,
            ),


            )
    } else {
        if( isTitle){
            Text(text,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 30.sp,
                lineHeight = 35.sp,
                    color = MaterialTheme.colorScheme.onPrimary
            ))
        }else{
            ExpandableText(text)

        }




    }
}



@Composable
fun ExpandableText(text: String) {
    val maxLength = 500
    val showFullText = remember { mutableStateOf(false) }


    val displayText = if (showFullText.value || text.length <= maxLength) {
        text
    } else {
        text.take(maxLength) + "..."
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = displayText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .animateContentSize( // Animate khi size của Text thay đổi
                    animationSpec = tween(durationMillis = 500)
                ),
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onPrimary
        )

        if (text.length > maxLength) {
            Text(
                text = if (showFullText.value) "Close" else "Show More",
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable {
                        showFullText.value = !showFullText.value
                    }
                    .padding(8.dp),
                textAlign = TextAlign.Start,
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )
        }
    }
}




@Composable
fun CategoryAndPriorityMenu(detailViewModel: DetailViewModel, note: Note) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val listCategory = detailViewModel.listCategory
    val categoryMenuExpanded by detailViewModel.categoryMenuExpanded
    val listPriority = detailViewModel.listPriority
    val priorityMenuExpanded by detailViewModel.priorityMenuExpanded



    Row(
        modifier = Modifier.fillMaxWidth(),


        ) {


        if (categoryMenuExpanded) {
            AlertDialog(
                onDismissRequest = { detailViewModel.updateCategoryMenuExpended(false) },
                title = {
                    Text(text = "Select category")
                },
                text = {
                    Column {
                        listCategory.forEach { category ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        detailViewModel.updateNote(
                                            note.copy(category = category.nameOrId)
                                        )
                                        detailViewModel.updateCategoryMenuExpended(false)
                                    }
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = category.icon!!),
                                    contentDescription = null,
                                    tint = primaryColor,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = category.nameOrId,
                                    fontSize = 16.sp,
                                    color = primaryColor
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { detailViewModel.updateCategoryMenuExpended(false) }) {
                        Text("Close")
                    }
                }
            )
        }



        Spacer(Modifier.weight(1f))



        if (priorityMenuExpanded) {
            AlertDialog(
                onDismissRequest = { detailViewModel.updatePriorityMenuExpanded(false) },
                title = {
                    Text(text = "Select priority")
                },
                text = {
                    Column {

                        listPriority.forEach { priority ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        detailViewModel.updateNote(
                                            note.copy(priority = priority.nameOrId.toInt())
                                        )
                                        detailViewModel.updatePriorityMenuExpanded(false)
                                    }
                                    .background(priority.color!!)
                                    .padding(vertical = 12.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = priority.nameOrId,
                                    textAlign = TextAlign.Center,
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { detailViewModel.updatePriorityMenuExpanded(false) }) {
                        Text("Close")
                    }
                }
            )
        }

    }
}


@Composable
fun DateTimeRow(
    note: Note,

) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(Modifier.width(5.dp))
            Text(
                note.dateNotify,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(Modifier.width(5.dp))
            Text(
                note.timeNotify,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )



    }
}


