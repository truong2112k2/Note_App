package com.example.noteapp.ui.presentation.home

import android.content.Context
import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.noteapp.R
import com.example.noteapp.common.Constants
import com.example.noteapp.ui.presentation.add_note.CreateATitle
import com.example.noteapp.ui.theme.gray
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel

    ){

    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

    val isDarkTheme by homeViewModel.isDarkTheme.collectAsState()

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


            IconButton(onClick = {

                navController.navigate(Constants.ADD_NOTE_ROUTE)
            }) {
                Icon( imageVector = Icons.Default.Add, contentDescription = "", tint = onPrimaryColor)
            }
        }

    )

}



@Composable
fun SearchBar(addNoteViewModel: HomeViewModel){

    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(onPrimaryColor, RoundedCornerShape(8.dp)) // Tạo nền trắng và bo góc
            .clip(RoundedCornerShape(20.dp)),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        TextField( value = addNoteViewModel.searchText.value  ,

            onValueChange ={ addNoteViewModel.updateSearchText(it) }
            ,
            modifier = Modifier.weight(1f),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent, // Xóa viền khi focus
                unfocusedIndicatorColor = Color.Transparent, // Xóa viền khi không focus
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor =  primaryColor
            ),
            textStyle = TextStyle(color = primaryColor),
            leadingIcon = { Icon( painter = painterResource(R.drawable.ic_search), contentDescription = "", Modifier.size(28.dp), tint = primaryColor) }
        )


    }

}

@Composable
fun LoadImageFromFile(context: Context, fileName: String) {
    val file = File(File(context.filesDir, "image"), fileName)

    if (file.exists()) {
        Image(
            painter = rememberAsyncImagePainter(file),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}
