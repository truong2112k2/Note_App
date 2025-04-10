package com.example.noteapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.common.Constants
import com.example.noteapp.ui.presentation.CustomNavigation

import com.example.noteapp.ui.theme.NoteAppTheme
import com.example.noteapp.ui.presentation.add_note.AddNoteViewModel
import com.example.noteapp.ui.presentation.add_note.ComposeDatePicker
import com.example.noteapp.ui.presentation.add_note.CustomTimePicker
import com.example.noteapp.ui.presentation.add_note.NotificationRow
import com.example.noteapp.ui.presentation.home.HomeViewModel
import com.example.noteapp.ui.theme.CustomNoteAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
private val addNoteViewModel: AddNoteViewModel by viewModels() // khai báo viewmodel
private val homeViewModel: HomeViewModel by viewModels() // khai báo viewmodel
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by homeViewModel.isDarkTheme.collectAsState()
            val context = LocalContext.current

            CustomNoteAppTheme( useDarkTheme = isDarkTheme) {



                val navController = rememberNavController()


              CustomNavigation(navController,context, homeViewModel)



            }
        }
    }
}











@Composable
fun RequestNotificationPermission(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted ->
                if (!granted) {
                    Toast.makeText(context, "Bạn cần cấp quyền thông báo!", Toast.LENGTH_SHORT).show()
                }
            }
        )

        LaunchedEffect(Unit) {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("RememberReturnType", "DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi =true )
@Composable
fun GreetingPreview(addNoteViewModel: AddNoteViewModel = hiltViewModel()) {
    NoteAppTheme {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .statusBarsPadding()
//                .padding(8.dp)
//                .verticalScroll(rememberScrollState())
//            ,
//            horizontalAlignment = Alignment.CenterHorizontally,
//
//            ){
//
//            val titleNote by addNoteViewModel.titleNote
//            val contentNote by addNoteViewModel.contentNote
//
//            val listCategory = addNoteViewModel.listCategory
//            val selectCategory by addNoteViewModel.selectedCategory
//            val categoryMenuExpanded by addNoteViewModel.categoryMenuExpanded
//
//            val listPriority = addNoteViewModel.listPriority
//            val priorityMenuExpanded by addNoteViewModel.priorityMenuExpanded
//            val selectPriority by addNoteViewModel.selectedPriority
//
//
//            val showTimePicker by addNoteViewModel.showPickerTime
//            val selectedTime by addNoteViewModel.selectedTime
//            val selectedDate by addNoteViewModel.selectedDate
//
//            CenterAlignedTopAppBar(
//                title = {}
//                ,
//                navigationIcon = {
//                    IconButton(onClick = {
//
//                    }) {
//                        Icon( imageVector = Icons.Default.ArrowBack, contentDescription = "")
//                    }
//                },
//                actions = {
//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        horizontalArrangement = Arrangement.SpaceAround,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_image),
//                            contentDescription = "Icon 1",
//                            modifier = Modifier.size(24.dp).clickable {  }
//                        )
//
//                        Icon(
//                            imageVector = Icons.Default.DateRange,
//                            contentDescription = "Icon 2",
//                            modifier = Modifier.size(24.dp).clickable {
//                                addNoteViewModel.updateShowPickerDate(true)
//                            }
//                        )
//                        Icon(
//                            painter = painterResource(id = R.drawable.ic_time),
//                            contentDescription = "Icon 3",
//                            modifier = Modifier.size(24.dp).clickable {
//                                addNoteViewModel.updateShowPickerTime(true)
//                                Log.d("CheckValue", selectedDate)
//                            }
//                        )
//                        Icon(
//                            imageVector = Icons.Default.Check,
//                            contentDescription = "Icon 4",
//                            modifier = Modifier.size(24.dp).clickable {  }
//                        )
//                    }
//
//
//
//                }
//
//            )
//            if(selectedTime != "00:00" || selectedDate != "00/00/0000" ){
//                NotificationRow(selectedDate, selectedTime, Icons.Default.Notifications)
//            }
//
//            Spacer(Modifier.height(8.dp))
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//
//            ) {
//                Box(
//
//                ) {
//
//                    Text(
//                        text = selectCategory,
//                        modifier = Modifier
//                            .width(150.dp)
//                            .border(1.dp, Color.Black, CircleShape)
//                            .padding(8.dp)
//                            .clickable {
//                                addNoteViewModel.updateCategoryMenuExpended(!categoryMenuExpanded)
//                                Log.d(Constants.STATUS_TAG, "Expanded = $categoryMenuExpanded")
//                            },
//                        textAlign = TextAlign.Center
//                    )
//
//                    DropdownMenu(
//                        expanded = categoryMenuExpanded,
//                        onDismissRequest = {addNoteViewModel.updateCategoryMenuExpended(false)},
//                        offset = DpOffset(x = 0.dp, y = 10.dp) // 👈 Lệch xuống 10.dp
//
//
//                    ) {
//                        listCategory.forEach { category->
//                            DropdownMenuItem(
//                                text = {
//                                    Text(
//                                        text = category.nameOrId,
//                                        fontSize = 16.sp,
//                                        color = Color.Black
//                                    )
//                                       },
//                                onClick = {
//                                    addNoteViewModel.updateSelectCategory(category.nameOrId)
//                                    addNoteViewModel.updateCategoryMenuExpended(!categoryMenuExpanded)
//                                },
//                                leadingIcon = {
//                                           Icon(
//                                                painter = painterResource(id = category.icon!!),
//                                                contentDescription = "",
//                                                tint = Color.Black,
//                                                modifier = Modifier.size(24.dp)
//                                            )
//                                }
//                            )
//
//                        }
//
//                    }
//
//                }
//
//                Spacer(Modifier.weight(1f))
//
//
//                Box(
//
//                ) {
//
//                    Text(selectPriority.nameOrId,
//                        modifier = Modifier
//                            .width(150.dp)
//                            .background(selectPriority.color!!, CircleShape)
//                            .border(1.dp, Color.Black, CircleShape)
//                            .padding(8.dp)
//                            .clickable {
//                                addNoteViewModel.updatePriorityMenuExpanded(!priorityMenuExpanded)
//                            },
//                          textAlign = TextAlign.Center
//
//                    )
//
//                    DropdownMenu(
//                        expanded = priorityMenuExpanded,
//                        onDismissRequest = {addNoteViewModel.updatePriorityMenuExpanded(false)},
//                        modifier = Modifier.padding(PaddingValues(0.dp))
//                    ) {
//                        listPriority.forEach { priority->
//                            DropdownMenuItem(
//                                text = {
//                                    Text(
//                                     text = priority.nameOrId, modifier = Modifier
//                                            .background(priority.color!!)
//                                            .fillMaxSize(),
//                                        textAlign = TextAlign.Center
//                                    )
//
//
//                                       },
//                                onClick = {
//                                    addNoteViewModel.updateSelectPriority(priority)
//                                    addNoteViewModel.updatePriorityMenuExpanded(!priorityMenuExpanded)
//                                          },
//                                modifier = Modifier
//                                    .fillMaxSize()
//                                    .background(priority.color!!),
//
//                            )
//
//                        }
//
//                    }
//
//                }
//            }
//
//            Spacer(Modifier.height(8.dp))
//
//            OutlinedTextField(
//                value = titleNote,
//                onValueChange = { addNoteViewModel.updateTitleNote(it) },
//                placeholder = {Text(text = "Title",style = MaterialTheme.typography.displayLarge, color = Color.LightGray)},
//                modifier = Modifier.fillMaxWidth(),
//                singleLine = true,
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = Color.Transparent,
//                    unfocusedBorderColor = Color.Transparent,
//                    disabledBorderColor = Color.Transparent,
//                    errorBorderColor = Color.Transparent
//                ),
//                textStyle = TextStyle(fontSize = 34.sp)
//
//            )
//            Spacer(Modifier.height(8.dp))
//
//
//
//
//
//            Row(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text("Content", style = MaterialTheme.typography.titleLarge)
//
//            }
//
//            val lineHeight = 24.sp
//
//
//            BasicTextField(
//                value = contentNote,
//                onValueChange = { addNoteViewModel.updateContentNote(it) },
//                modifier = Modifier
//                    .fillMaxSize()
//                    .heightIn(150.dp)
//                    .padding(8.dp),
//                textStyle = TextStyle(
//                    fontSize = 16.sp,
//                    lineHeight = lineHeight,
//                    color = Color.Black
//                ),
//                decorationBox = { innerTextField ->
//                    Box {
//                        // Layer vẽ gạch chân từng dòng
//                        Canvas(modifier = Modifier.matchParentSize()) {
//                            val lineSpacing = lineHeight.toPx()
//                            var currentY = 0f
//                            while (currentY < size.height) {
//                                drawLine(
//                                    color = Color.LightGray,
//                                    start = Offset(0f, currentY + lineSpacing),
//                                    end = Offset(size.width, currentY + lineSpacing),
//                                    strokeWidth = 1f
//                                )
//                                currentY += lineSpacing
//                            }
//                        }
//                        innerTextField() // TextField content
//                    }
//                }
//            )
//
//            if(showTimePicker){
//                CustomTimePicker(
//                    onConfirm = { timeState->
//                        val hour = timeState.hour
//                        val minute = timeState.minute
//                        addNoteViewModel.updateShowPickerTime(false)
//                        addNoteViewModel.updateSelectedTime(String.format("%02d:%02d", hour, minute))
//                    },
//                    onDismiss = {
//                        addNoteViewModel.updateShowPickerTime(false)
//
//                    }
//                )
//            }
//
//            ComposeDatePicker()
//
//
//
//
//
//        }
//    }
    }
}
