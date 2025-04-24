package com.example.noteapp.presentation.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Checkbox
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.noteapp.common.Constants
import com.example.noteapp.domain.model.Note
import com.example.noteapp.ui.background.GradientBackground
import com.example.noteapp.presentation.detail.DisplayEmptyListMessage
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@SuppressLint("NewApi")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController, context: Context, homeViewModel: HomeViewModel) {

    val listNote by homeViewModel.listNote.collectAsState()
    val homeNoteState = homeViewModel.homeState.value
    var animationVisible by remember { mutableStateOf(false) }

    val isListMode by homeViewModel.isListMode
    LaunchedEffect(Unit) {
       homeViewModel.getAllNote()

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        GradientBackground()


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            if (homeNoteState.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                }
            } else {


                TopBarHomeScreen(navController, homeViewModel)

                LaunchedEffect(Unit) {
                    // Delay nhỏ để hiệu ứng lần lượt
                    delay(100)
                    animationVisible = true
                }

                if (
                    homeNoteState.isSuccess

                ) {

                    SearchBar(homeViewModel)
                    Spacer(Modifier.height(10.dp))

                    Column {
                        LazyVerticalStaggeredGrid(
                            columns = StaggeredGridCells.Fixed(2),
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            verticalItemSpacing = 8.dp,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(listNote.size) { index ->

                                val note = listNote[index]


                                AnimatedVisibility(
                                    visible = animationVisible,
                                    enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                                        initialOffsetY = { it / 2 },
                                        animationSpec = tween(500)
                                    ),
                                    exit = fadeOut()
                                ) {

                                    AnimatedContent(
                                        targetState = isListMode,
                                        label = "ModeSwitcherAnimation"
                                    ) { listMode ->
                                        if (listMode) {
                                            NoteItemSelected(
                                                note = note,
                                                isSelected = homeViewModel.selectedNoteIds.value.contains(note.id),
                                                onToggleSelect = { homeViewModel.toggleSelection(note.id) }
                                            )
                                        } else {
                                            NoteItem(
                                                note,
                                                onClickItem = {
                                                    navController.navigate("${Constants.DETAIL_NOTE_ROUTE}/${note.id}")
                                                }
                                            )
                                        }
                                    }


                                }
                            }

                        }

                       if(isListMode){
                           FloatingActionButton(
                               onClick = {

                                   homeViewModel.deleteNotes(context)

                               },
                               containerColor = MaterialTheme.colorScheme.onPrimary,
                               contentColor = Color.White,
                               modifier = Modifier
                                   .align(alignment = Alignment.CenterHorizontally)
                                   .padding(20.dp)
                                   .size(65.dp)
                           ) {

                               Icon(Icons.Default.DeleteForever, contentDescription = "Delete",
                                   tint = MaterialTheme.colorScheme.primary )
                           }
                       }else{
                           FloatingActionButton(
                               onClick = {
                                   navController.navigate(Constants.ADD_NOTE_ROUTE)

                               },
                               containerColor = MaterialTheme.colorScheme.onPrimary,
                               contentColor = Color.White,
                               modifier = Modifier
                                   .align(alignment = Alignment.CenterHorizontally)
                                   .padding(20.dp)
                                   .size(65.dp)
                           ) {
                               Icon(Icons.Default.Add, contentDescription = "Add",
                                   tint = MaterialTheme.colorScheme.primary )
                           }
                       }

                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp)
                            .statusBarsPadding(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        DisplayEmptyListMessage(navController)
                    }

                }

            }


        }


    }


}




@Composable
fun NoteItem(
    note: Note,
    onClickItem:()->Unit) {



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClickItem()
            }
        ,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(
            alpha = 0.65f

        ))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(4.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text =note.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(note.height.dp))
            Text(
                text =note. content,
                fontWeight = FontWeight.Normal,
                color =  Color.Black,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

        }
    }
}


@Composable
fun NoteItemSelected(
    note: Note,
    isSelected: Boolean,
    onToggleSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {  onToggleSelect() }
        ,
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.65f)
        )
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onToggleSelect()},
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Transparent,        // màu nền khi đã chọn
                        uncheckedColor = MaterialTheme.colorScheme.onPrimary,        // màu viền khi chưa chọn
                        checkmarkColor =MaterialTheme.colorScheme.onPrimary       // màu của dấu tick
                    )
                )


            }
        }
        Column(modifier = Modifier.padding(16.dp)) {




                Text(
                    text = note.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(note.height.dp))
                Text(
                    text = note.content,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

            }

    }
}
