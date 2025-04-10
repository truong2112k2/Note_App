package com.example.noteapp.ui.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.noteapp.R
import com.example.noteapp.common.Constants
import com.example.noteapp.ui.presentation.add_note.CreateATitle
import com.example.noteapp.ui.theme.gray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHomeScreen(
    navController: NavController,

    ){
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary, // màu nền
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
                Icon( imageVector = Icons.Default.ArrowBack, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary)
            }
        },
        actions = {
            IconButton(onClick = {

                navController.navigate(Constants.ADD_NOTE_ROUTE)
            }) {
                Icon( imageVector = Icons.Default.Add, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }

    )

}



@Composable
fun SearchBar(addNoteViewModel: HomeViewModel){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(gray, RoundedCornerShape(8.dp)) // Tạo nền trắng và bo góc
            .clip(RoundedCornerShape(20.dp)),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        TextField( value = addNoteViewModel.searchText.value  ,

            onValueChange ={ addNoteViewModel.updateSearchText(it) }
            , modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent, // Xóa viền khi focus
                unfocusedIndicatorColor = Color.Transparent, // Xóa viền khi không focus
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            leadingIcon = { Icon( painter = painterResource(R.drawable.ic_search), contentDescription = "", Modifier.size(28.dp), tint = MaterialTheme.colorScheme.onPrimary) }
        )


    }

}