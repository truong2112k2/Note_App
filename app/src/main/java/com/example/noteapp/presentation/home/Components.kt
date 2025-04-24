package com.example.noteapp.presentation.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.noteapp.R
import com.example.noteapp.presentation.CustomDatePicker
import com.example.noteapp.presentation.add_note.CreateATitle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel

) {

    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

    val isDarkTheme by homeViewModel.isDarkTheme
    val isShowSearchView by homeViewModel.showSearchView
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent, // màu nền
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title =
        {
            CreateATitle("Home")
        },
        navigationIcon = {
            IconButton(onClick = {

            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    tint = onPrimaryColor
                )
            }
        },
        actions = {
            // button doi theme
            IconButton(
                onClick = {
                    homeViewModel.toggleTheme()
                }
            ) {
                Icon(
                    painter = painterResource(if (isDarkTheme) R.drawable.ic_dark_theme else R.drawable.ic_light_theme),
                    contentDescription = "",
                    tint = onPrimaryColor
                )
            }


            IconButton(
                onClick = {
                    homeViewModel.toggleSearchView()
                }
            ) {
                Icon(
                    painter = painterResource(if (isShowSearchView) R.drawable.ic_cancel else R.drawable.ic_search),
                    contentDescription = "",
                    tint = onPrimaryColor
                )
            }
            val isListMode by homeViewModel.isListMode

            IconButton(onClick = {
              homeViewModel.toggleListMode()

                Log.d("Check mode List","Click List Mode = ${isListMode}")

            },
                Modifier.background(
                    if(isListMode) MaterialTheme.colorScheme.onPrimary.copy(
                        alpha = 0.3f
                    ) else Color.Transparent ,
                    CircleShape)
            ) {
                Icon( imageVector = Icons.Default.Checklist, contentDescription = "", tint = onPrimaryColor)
            }
        }

    )

}

@SuppressLint("NewApi", "SuspiciousIndentation")
@Composable
fun SearchBar(homeViewModel: HomeViewModel) {
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val isShowSearchView by homeViewModel.showSearchView
    val isShowDatePicker by homeViewModel.isShowDatePicker
    val selectedDate by homeViewModel.selectedDate

 //   if (isShowSearchView) {
        AnimatedVisibility(
            visible = isShowSearchView,
            enter = fadeIn(tween(300)) + scaleIn(tween(300)) + slideInVertically(initialOffsetY = { -it / 3 }),
            exit = fadeOut(tween(300)) + scaleOut(tween(300)) + slideOutVertically(targetOffsetY = { -it / 3 })
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent, RoundedCornerShape(8.dp))
                        .border(1.dp, onPrimaryColor, RoundedCornerShape(8.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextField(
                        value = homeViewModel.searchText.value,
                        onValueChange = {
                            homeViewModel.updateSearchText(it)
                            homeViewModel.searchNoteByTitle(it)
                        },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            cursorColor = onPrimaryColor
                        ),
                        textStyle = TextStyle(color = onPrimaryColor),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(28.dp)
                                    .clickable { homeViewModel.updateShowDatePicker(true) },
                                tint = onPrimaryColor
                            )
                        },
                        singleLine = true,
                        label = { Text("By name", color = onPrimaryColor) }
                    )
                }

                if (selectedDate.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filter by : ${selectedDate}",
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Icon",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.clickable {
                                homeViewModel.updateSelectedDate("")
                                homeViewModel.getAllNote()
                            }
                        )
                    }
                }
            }
        }

//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.Transparent, RoundedCornerShape(8.dp)) // Tạo nền trắng và bo góc
//                .border(1.dp, onPrimaryColor, RoundedCornerShape(8.dp)),
//
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
//        ) {
//
//            TextField(
//                value = homeViewModel.searchText.value,
//
//                onValueChange = {
//                    homeViewModel.updateSearchText(it)
//                    homeViewModel.searchNoteByTitle(it)
//                },
//                modifier = Modifier.weight(1f),
//                colors = TextFieldDefaults.colors(
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent,
//                    focusedContainerColor = Color.Transparent,
//                    unfocusedContainerColor = Color.Transparent,
//                    cursorColor = onPrimaryColor
//
//                ),
//                textStyle = TextStyle(color = onPrimaryColor),
//                trailingIcon =
//                {
//
//
//                    Icon(
//                        imageVector = Icons.Default.DateRange,
//                        contentDescription = "",
//                        Modifier
//                            .size(28.dp)
//                            .clickable {
//
//                                homeViewModel.updateShowDatePicker(true)
//
//                            },
//                        tint = onPrimaryColor)
//
//
//
//                },
//                singleLine = true,
//                label = { Text("By name", color = onPrimaryColor) },
//
//                )
//        }



//        if (selectedDate.isNotEmpty()) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "Filter by : ${selectedDate}",
//                    color = MaterialTheme.colorScheme.onPrimary
//                )
//                Spacer(Modifier.weight(1f))
//                Icon(
//                    imageVector = Icons.Default.Close,
//                    contentDescription = "Icon",
//                    tint = MaterialTheme.colorScheme.onPrimary,
//                    modifier = Modifier.clickable {
//                        homeViewModel.updateSelectedDate("")
//                        homeViewModel.getAllNote()
//                    }
//                )
//            }
//        }


   // }

    CustomDatePicker(
        isShowDatePicker,
        onClickPositiveButton = { homeViewModel.updateShowDatePicker(false) },
        onClickNegativeButton = { homeViewModel.updateShowDatePicker(false) },
        onSelectDate = {
            homeViewModel.updateSelectedDate(it)

            homeViewModel.searchNoteByDate(it)
        }
    )


}



