package com.example.noteapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.ui.presentation.CustomNavigation
import com.example.noteapp.ui.presentation.add_note.AddNoteViewModel
import com.example.noteapp.ui.presentation.home.HomeViewModel
import com.example.noteapp.ui.theme.CustomNoteAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.io.encoding.Base64

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels() // khai báo viewmodel

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

//GreetingPreview()

            val isDarkTheme by homeViewModel.isDarkTheme.collectAsState()
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
        modifier = Modifier.fillMaxWidth().padding(16.dp).statusBarsPadding()

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

        Text(stringResource(R.string.welcome_message), textAlign = TextAlign.Start, modifier =  Modifier.padding(8.dp))
        Text(stringResource(R.string.note_feature_description), textAlign = TextAlign.Start,modifier =Modifier.padding(8.dp))
        Text(stringResource(R.string.no_notes_yet), textAlign = TextAlign.Start,modifier =Modifier.padding(8.dp))
        Text(stringResource(R.string.create_note_now), textAlign = TextAlign.Start,modifier =Modifier.padding(8.dp))
        Text(stringResource(R.string.inspiration_message), textAlign = TextAlign.Start,modifier =Modifier.padding(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().background(Color.LightGray, RoundedCornerShape(16.dp)).clip(RoundedCornerShape(16.dp)).padding(16.dp),
        ) {



            Text(
                text = "Create your first note!",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f)) // khoảng cách giữa image và text

            Image(
                imageVector =  Icons.Default.KeyboardArrowRight, // thay bằng icon của bạn
                contentDescription = "Note Icon",
                modifier = Modifier.size(40.dp)
            )
        }
    }

}
