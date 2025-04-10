package com.example.noteapp.ui.presentation

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.noteapp.common.Constants
import com.example.noteapp.ui.presentation.add_note.AddNoteScreen
import com.example.noteapp.ui.presentation.home.HomeScreen
import com.example.noteapp.ui.presentation.home.HomeViewModel

@SuppressLint("NewApi")
@Composable
fun CustomNavigation(
    navController: NavHostController,
    context: Context,
    homeViewModel: HomeViewModel
) {

NavHost(navController = navController, startDestination = Constants.HOME_ROUTE) {
    composable(Constants.HOME_ROUTE){
        HomeScreen(navController, context, homeViewModel)
    }
    composable(Constants.ADD_NOTE_ROUTE) {
        AddNoteScreen(context, navController)
    }
}
}