package com.example.noteapp.ui.presentation.home

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController, context: Context, homeNoteViewModel: HomeViewModel){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(MaterialTheme.colorScheme.primary)
            .padding(16.dp)
    ) {

        val isDarkTheme by homeNoteViewModel.isDarkTheme.collectAsState()
        Button(onClick ={
            homeNoteViewModel.toggleTheme()
        }) {
            Text( text = if (isDarkTheme) {"Dark"}  else "Light")
        }
        TopBarHomeScreen(navController)
        SearchBar(homeNoteViewModel)


    }

}