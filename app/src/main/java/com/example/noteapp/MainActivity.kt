package com.example.noteapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.common.Constants
import com.example.noteapp.presentation.CustomNavigation
import com.example.noteapp.presentation.home.viewmodel.HomeViewModel
import com.example.noteapp.ui.theme.CustomNoteAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeViewModel: HomeViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {


            val isDarkTheme by homeViewModel.uiState.isDarkTheme
            val context = LocalContext.current

            CustomNoteAppTheme(useDarkTheme = isDarkTheme) {

                val navController = rememberNavController()

                CustomNavigation(navController, context, homeViewModel)


            }

        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("RememberReturnType", "DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview(
) {


}

