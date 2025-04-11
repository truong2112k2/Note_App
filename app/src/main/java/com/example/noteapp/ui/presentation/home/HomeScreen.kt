package com.example.noteapp.ui.presentation.home

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.noteapp.ui.background.GradientBackground
import com.example.noteapp.ui.background.generateRandomNonBlackWhiteColor
import kotlinx.coroutines.delay


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController, context: Context, homeNoteViewModel: HomeViewModel) {

    LaunchedEffect(Unit) {
        homeNoteViewModel.getAllNote()

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

            val listNote by homeNoteViewModel.listNote.collectAsState()
            TopBarHomeScreen(navController, homeNoteViewModel)
            SearchBar(homeNoteViewModel)

            Spacer(Modifier.height(10.dp))
            var visible by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                // Delay nhỏ để hiệu ứng lần lượt
                delay(100)
                visible = true

            }





            if(

                listNote.isNotEmpty() &&
                homeNoteViewModel.colors.size == listNote.size &&
                homeNoteViewModel.heights.size == listNote.size

                )

            {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalItemSpacing = 8.dp,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listNote.size) { index ->

                        val note = listNote[index]
                        val color = homeNoteViewModel.colors[note.id.toInt()] ?: Color.Gray
                        val height = homeNoteViewModel.heights[note.id.toInt()] ?: 40.dp

                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
                                initialOffsetY = { it / 2 },
                                animationSpec = tween(500)
                            ),
                            exit = fadeOut()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(4.dp, RoundedCornerShape(16.dp))
                                    .background(color, RoundedCornerShape(16.dp))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                ) {
                                    Text(
                                        text = note.title,
                                        style = MaterialTheme.typography.labelLarge,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(modifier = Modifier.height(height))

                                    Text(
                                        text = note.dateAdd,
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }

//                    items(listNote.size) { index ->
//                        AnimatedVisibility(
//                            visible = visible,
//                            enter = fadeIn(animationSpec = tween(500)) + slideInVertically(
//                                initialOffsetY = { it / 2 },
//                                animationSpec = tween(500)
//                            ),
//                            exit = fadeOut()
//                        ) {
//
//                            Box(
//                                modifier = Modifier
//
//                                    .fillMaxWidth()
//                                    .shadow(4.dp, RoundedCornerShape(16.dp))
//                                  //  .background(randomColor[index], RoundedCornerShape(16.dp))
//                                   .background(homeNoteViewModel.colors[index], RoundedCornerShape(16.dp))
//
//
//                            ) {
//
//                                Column(
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .padding(10.dp)
//                                ) {
//                                    Text(
//                                        text = listNote[index].title,
//                                        style = MaterialTheme.typography.labelLarge ,
//                                        maxLines = 3,
//                                        overflow = TextOverflow.Ellipsis
//                                    )
//
//                                   Spacer(modifier = Modifier.height(homeNoteViewModel.heights[index]))
//
//                                    Text(
//                                        text = listNote[index].dateAdd,
//                                        style = MaterialTheme.typography.bodyMedium,
//                                        maxLines = 1, // hoặc bao nhiêu dòng tùy
//                                        overflow = TextOverflow.Ellipsis
//                                    )
//                                }
//
//                            }
//                        }
//
//
//                    }
                }
            }else{
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No notes yet")
                }
            }

            }


    }
}




