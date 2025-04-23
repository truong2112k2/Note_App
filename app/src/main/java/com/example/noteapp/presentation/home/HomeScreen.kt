package com.example.noteapp.presentation.home

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.noteapp.common.Constants
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
                            val color = listNote[index].color
                            val height = listNote[index].height

                            AnimatedVisibility(
                                visible = animationVisible,
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
                                        .background(Color(color), RoundedCornerShape(16.dp))
                                        .clickable {
                                            navController.navigate("${Constants.DETAIL_NOTE_ROUTE}/${note.id}")
                                        }
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


                                        val dateString = note.dateAdd // "02/11/2002"

                                        val originalFormatter = DateTimeFormatter.ofPattern(
                                            "dd/MM/yyyy",
                                            Locale.ENGLISH
                                        )
                                        val localDate =
                                            LocalDate.parse(dateString, originalFormatter)


                                        val newFormatter = DateTimeFormatter.ofPattern(
                                            "MMM dd, yyyy",
                                            Locale.ENGLISH
                                        )
                                        val formattedDate = localDate.format(newFormatter)

                                        Spacer(modifier = Modifier.height(height.dp))

                                        Text(
                                            text = formattedDate, // Sử dụng ngày đã định dạng
                                            style = MaterialTheme.typography.bodyMedium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                    }
                                }
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




