package com.example.noteapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.domain.model.Note
import com.example.noteapp.ui.presentation.CustomNavigation

import com.example.noteapp.ui.theme.NoteAppTheme
import com.example.noteapp.ui.presentation.add_note.AddNoteViewModel
import com.example.noteapp.ui.presentation.home.HomeViewModel
import com.example.noteapp.ui.theme.CustomNoteAppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.random.Random

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
private val addNoteViewModel: AddNoteViewModel by viewModels() // khai báo viewmodel
private val homeViewModel: HomeViewModel by viewModels() // khai báo viewmodel
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by homeViewModel.isDarkTheme.collectAsState()
            val context = LocalContext.current

            CustomNoteAppTheme( useDarkTheme = isDarkTheme) {






                val navController = rememberNavController()


            CustomNavigation(navController,context, homeViewModel)



            }
          //  GreetingPreview()
        }
    }
}













@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("RememberReturnType", "DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi =true )
@Composable
fun GreetingPreview(addNoteViewModel: AddNoteViewModel = hiltViewModel()) {
    NoteAppTheme {



    }
}
