package com.example.noteapp.ui.presentation.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.noteapp.R
import com.example.noteapp.ui.background.GradientBackground

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun DetailNoteScreen(idNote: String, detailViewModel: DetailViewModel = hiltViewModel()) {


    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val colorPrimary = MaterialTheme.colorScheme.primary
    var visible by remember { mutableStateOf(false) }
    val context = LocalContext.current


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

            val note by detailViewModel.note.collectAsState()
            val switchTopAppBar by detailViewModel.switchTopAppBar




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
                        switchTopAppBar = switchTopAppBar
                    )
                } else {
                    ViewTopAppBar(
                        detailViewModel = detailViewModel,
                        switchTopAppBar = switchTopAppBar
                    )
                }
            }

            LoadImageFromFile(context, note.image.toString())
            Text(
                text = note.content,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            EditableText(
                note.content,
                onTextChange = {
                    detailViewModel.updateNote(
                        note.copy(
                            content = it
                        )
                    )
                },
                switchTopAppBar
            )

            /*

            val title: String,
            val content: String,
            val dateAdd: String, // Unix timestamp (time in milliseconds)
            val category: String,
            val priority: Int, // Priority can be 1 (high), 2 (medium), 3 (low), etc.
            val image: String?, // Image URL or path to the image
            val timeNotify: String,// Unix timestamp for notification time
            val dateNotify: String //  ngày tạo (ví dụ: "2025-04-08")

             */
            //  val content by detailViewModel.content


        }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTopAppBar(detailViewModel: DetailViewModel, switchTopAppBar: Boolean) {
    val note by detailViewModel.note.collectAsState()

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
            Text(
                note.title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

        },
        navigationIcon = {
            IconButton(onClick = { }) {
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
                        .clickable { }
                )
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { }
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_time),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { }
                )
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            detailViewModel.updateSwitchTopAppBar(!switchTopAppBar)
                        }
                )
            }
        }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewTopAppBar(detailViewModel: DetailViewModel, switchTopAppBar: Boolean) {
    val note by detailViewModel.note.collectAsState()

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
            Text(
                note.title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { }) {
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


@Composable
fun EditableText(
    text: String,
    onTextChange: (String) -> Unit,
    isEditing: Boolean,

    ) {
    if (isEditing) {
        TextField(
            value = text,
            onValueChange = onTextChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,


            )
    } else {
        Text(
            text = text,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}
