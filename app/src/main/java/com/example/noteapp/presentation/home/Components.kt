package com.example.noteapp.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.noteapp.common.Constants
import com.example.noteapp.presentation.CustomDatePicker
import com.example.noteapp.presentation.add_note.CreateATitle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel

    ){

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
        }
        ,
        navigationIcon = {
            IconButton(onClick = {

            }) {
                Icon( imageVector = Icons.Default.ArrowBack, contentDescription = "", tint = onPrimaryColor)
            }
        },
        actions = {
            // button doi theme
            IconButton(
                onClick = {
                  homeViewModel.toggleTheme()
                }
            ) {
                Icon(  painter = painterResource(if (isDarkTheme) R.drawable.ic_dark_theme else R.drawable.ic_light_theme), contentDescription = "", tint = onPrimaryColor)
            }


            IconButton(
                onClick = {
                    homeViewModel.toggleSearchView()
                }
            ) {
                Icon(  painter = painterResource(if (isShowSearchView) R.drawable.ic_cancel else R.drawable.ic_search), contentDescription = "", tint = onPrimaryColor)
            }


            IconButton(onClick = {

                navController.navigate(Constants.ADD_NOTE_ROUTE)
            }) {
                Icon( imageVector = Icons.Default.Add, contentDescription = "", tint = onPrimaryColor)
            }
        }

    )

}

@SuppressLint("NewApi")
@Composable
fun SearchBar(homeViewModel: HomeViewModel){
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val isShowSearchView by homeViewModel.showSearchView
    val isShowDatePicker by homeViewModel.isShowDatePicker
    val selectedDate by homeViewModel.selectedDate
    val resultSearch by homeViewModel.resultSearch

    if(isShowSearchView){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent, RoundedCornerShape(8.dp)) // Tạo nền trắng và bo góc
                .border(1.dp, onPrimaryColor, RoundedCornerShape(8.dp))
            ,

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            TextField( value = homeViewModel.searchText.value  ,

                onValueChange ={
                    homeViewModel.updateSearchText(it)
                    homeViewModel.searchNoteByTitle(it)
                               }
                ,
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor =  onPrimaryColor

                ),
                textStyle = TextStyle(color = onPrimaryColor),
                trailingIcon =
                {
                    Icon(
                        imageVector = Icons.Default.DateRange, contentDescription = ""
                        , Modifier.size(28.dp).clickable {

                            homeViewModel.updateShowDatePicker(true)

                        }
                        , tint = onPrimaryColor)
                },
                singleLine = true,
                label = { Text("By name", color = onPrimaryColor) },

            )
        }


//        if(resultSearch.isNotEmpty()){
//            Text(resultSearch, modifier = Modifier.fillMaxWidth().padding(8.dp))
//        }

        if(selectedDate.isNotEmpty()){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Filter by : ${selectedDate}", color = MaterialTheme.colorScheme.onPrimary )
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

       CustomDatePicker(
           isShowDatePicker,
           onClickPositiveButton = {homeViewModel.updateShowDatePicker(false)},
           onClickNegativeButton = {homeViewModel.updateShowDatePicker(false)},
           onSelectDate = {
               homeViewModel.updateSelectedDate(it)

               homeViewModel.searchNoteByDate(it)
           }
       )


}



