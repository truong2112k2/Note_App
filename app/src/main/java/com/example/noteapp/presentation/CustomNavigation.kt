package com.example.noteapp.presentation

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.noteapp.common.Constants
import com.example.noteapp.presentation.add_note.AddNoteScreen
import com.example.noteapp.presentation.detail.DetailNoteScreen
import com.example.noteapp.presentation.home.HomeScreen
import com.example.noteapp.presentation.home.viewmodel.HomeViewModel

@SuppressLint("NewApi")
@Composable
fun CustomNavigation(
    navController: NavHostController,
    context: Context,
    homeViewModel: HomeViewModel
) {

NavHost(navController = navController, startDestination = Constants.SPlASH_ROUTE) {
    composable(Constants.SPlASH_ROUTE){
        SplashScreen(navController)
    }
    composable(Constants.HOME_ROUTE){
        HomeScreen(navController, context, homeViewModel)
    }
    composable(Constants.ADD_NOTE_ROUTE) {
        AddNoteScreen(context, navController)
    }

    composable("${Constants.DETAIL_NOTE_ROUTE}/{idNote}") { backStackEntry ->
        val idNote = backStackEntry.arguments?.getString("idNote") ?: "0"

        DetailNoteScreen(idNote, navController)
    }
}
}