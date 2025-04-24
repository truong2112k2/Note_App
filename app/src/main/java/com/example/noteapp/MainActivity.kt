package com.example.noteapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.presentation.CustomNavigation
import com.example.noteapp.presentation.add_note.AddNoteViewModel
import com.example.noteapp.presentation.home.HomeViewModel
import com.example.noteapp.ui.theme.CustomNoteAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels() // khai báo viewmodel

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {



            val isDarkTheme by homeViewModel.isDarkTheme
            val context = LocalContext.current

            CustomNoteAppTheme(useDarkTheme = isDarkTheme) {


                val navController = rememberNavController()


                CustomNavigation(navController, context, homeViewModel)


            }

        }
    }
}

@Composable
fun EditableText() {
    var isEditing by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("Click to edit me") }

    if (isEditing) {
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
            trailingIcon = {
                IconButton(onClick = { isEditing = false }) {
                    Icon(Icons.Default.Check, contentDescription = "Done")
                }
            }
        )
    } else {
        Text(
            text = text,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isEditing = true }
                .padding(8.dp)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("RememberReturnType", "DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview(addNoteViewModel: AddNoteViewModel = hiltViewModel()) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .statusBarsPadding()

    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_language), // thay bằng icon của bạn
                contentDescription = "Note Icon",
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(12.dp)) // khoảng cách giữa image và text

            Text(
                text = "Create your first note!",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Text(
            stringResource(R.string.welcome_message),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            stringResource(R.string.note_feature_description),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            stringResource(R.string.no_notes_yet),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            stringResource(R.string.create_note_now),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            stringResource(R.string.inspiration_message),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(8.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .padding(16.dp),
        ) {


            Text(
                text = "Create your first note!",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f)) // khoảng cách giữa image và text

            Image(
                imageVector = Icons.Default.KeyboardArrowRight, // thay bằng icon của bạn
                contentDescription = "Note Icon",
                modifier = Modifier.size(40.dp)
            )
        }
    }

}

@Composable
fun CustomGrid(items: List<String>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            count = items.size,
            span = { index ->
                // Mỗi item thứ 5 sẽ chiếm 2 cột
                if ((index + 1) % 5 == 0) {
                    GridItemSpan(2)
                } else {
                    GridItemSpan(1)
                }
            }
        ) { index ->
            val item = items[index]
            val isFullSpan = (index + 1) % 5 == 0

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .height(if (isFullSpan) 120.dp else 80.dp)
                    .background(
                        if (isFullSpan) Color.Cyan else Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item)
            }
        }
    }

}

@Composable
fun AppBarWithRoundButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp) // Tăng chiều cao để chứa FAB
    ) {
        // Custom AppBar with curved cutout
        Canvas(modifier = Modifier.fillMaxWidth().height(80.dp)) {
            val width = size.width
            val height = size.height

            val cutoutRadius = 40.dp.toPx()
            val cutoutCenter = Offset(width / 2, height)

            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(0f, height)
                arcTo(
                    rect = Rect(
                        left = cutoutCenter.x - cutoutRadius,
                        top = cutoutCenter.y - cutoutRadius,
                        right = cutoutCenter.x + cutoutRadius,
                        bottom = cutoutCenter.y + cutoutRadius
                    ),
                    startAngleDegrees = 180f,
                    sweepAngleDegrees = -180f,
                    forceMoveTo = false
                )
                lineTo(width, height)
                lineTo(width, 0f)
                close()
            }

            drawPath(path, color = Color.Red)
        }

        // Circular Button over the cutout
        FloatingActionButton(
            onClick = { /* Handle click */ },
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-20).dp)
                .size(60.dp)
                .zIndex(1f),
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
        }
    }
}

