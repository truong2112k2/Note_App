
package com.example.noteapp.presentation.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.noteapp.common.Constants
import com.example.noteapp.data.toNote
import com.example.noteapp.presentation.detail.DisplayEmptyListMessage
import com.example.noteapp.presentation.home.viewmodel.HomeViewModel
import com.example.noteapp.ui.background.GradientBackground
import kotlinx.coroutines.delay

@SuppressLint("NewApi")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController, context: Context, homeViewModel: HomeViewModel) {


    val listNote by homeViewModel.listNote.collectAsState()
    val getListState by homeViewModel.getListState.collectAsState()
    var animationVisible by remember { mutableStateOf(false) }

    val uiState = homeViewModel.uiState
    val isShowConfirmDeleteDialog by uiState.isShowConfirmDeleteDialog
    val isListMode by homeViewModel.uiState.isListMode
    //val isListMode by uiState.isListMode
    val listIdNote by uiState.selectedNoteIds
    val gridState = rememberLazyStaggeredGridState()

    LaunchedEffect(Unit) {
        homeViewModel.getAllNote()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GradientBackground()

            if (getListState.isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize().statusBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                }
            } else {
                LaunchedEffect(Unit) {
                    delay(100)
                    animationVisible = true
                }

                if (getListState.isSuccess) {
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        TopBarHomeScreen(homeViewModel)
                        SearchBar(homeViewModel)
                        Spacer(Modifier.height(10.dp))

                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(2),
                            modifier = Modifier.padding(8.dp),
                            verticalItemSpacing = 8.dp,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            state = gridState
                        ) {
                            items(listNote.size) { index ->
                                val note = listNote[index]
                                AnimatedVisibility(
                                    visible = animationVisible,
                                    enter = fadeIn(tween(500)) + slideInVertically(initialOffsetY = { it / 2 }, animationSpec = tween(500)),
                                    exit = fadeOut()
                                ) {
                                    AnimatedContent(
                                        targetState = isListMode,
                                        label = "ModeSwitcherAnimation"
                                    ) { listMode ->
                                        if (listMode) {
                                            NoteItemSelected(
                                                note = note,
                                                isSelected = listIdNote.contains(note.id),
                                                onToggleSelect = { homeViewModel.toggleSelection(note.id) }
                                            )
                                        } else {
                                            NoteItem(
                                                note = note,
                                                onClickItem = {
                                                    navController.navigate("${Constants.DETAIL_NOTE_ROUTE}/${note.id}")
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (isListMode) {

                        FloatingActionButton(
                            onClick = {

                                homeViewModel.updateShowConfirmDeleteDialog(true)

                            },
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = Color.White,
                            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 16.dp, bottom = 50.dp).size(65.dp)
                        ) {
                            Icon(
                                Icons.Default.DeleteForever, contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {


                        FloatingActionButton(
                            onClick = { navController.navigate(Constants.ADD_NOTE_ROUTE) },
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = Color.White,
                            modifier = Modifier.align(Alignment.BottomEnd).padding(end = 16.dp, bottom = 50.dp).size(65.dp)
                        ) {
                            Icon(
                                Icons.Default.Add, contentDescription = "Add",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }


                } else {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp).statusBarsPadding(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        DisplayEmptyListMessage(navController)
                    }
                }
            }

        if (isShowConfirmDeleteDialog) {
            ShowConfirmDeleteDialog(
                isDeleteDialog = listIdNote.isNotEmpty(),
                onDeleteNote = {
                    homeViewModel.deleteNotes(
                        context,
                        onSuccess = {

                            Log.d("213213"," homeViewModel.toggleListMode() ${isListMode}")
                            homeViewModel.toggleListMode()
                            Log.d("213213"," homeViewModel.toggleListMode() 2 ${isListMode}")

                            Log.d("213213","  homeViewModel.clearSelection()")
                            homeViewModel.clearSelection()

                        }
                    )
                               },
                onCancel = {
                    homeViewModel.updateShowConfirmDeleteDialog(false)
                    homeViewModel.toggleListMode()

                },
                sumNotes = listIdNote.size
            )
        }


    }
}






