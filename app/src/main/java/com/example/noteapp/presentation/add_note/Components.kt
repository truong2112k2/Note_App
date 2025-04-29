package com.example.noteapp.presentation.add_note

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.noteapp.R
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar



@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CenterTopAppBar(
    navController: NavController,
    onPrimaryColor: Color,
    onPickImage : () -> Unit,
    onPickDate: () -> Unit,
    onPickTime: () -> Unit,
    onInsertNote: (context: Context) -> Unit,
    onRequestPermission:() -> Unit,
    context: Context
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        title = {
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.popBackStack()

                }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    tint = onPrimaryColor
                )
            }
        },
        actions = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(20.dp))

                Icon(
                    painter = painterResource(id = R.drawable.ic_image),
                    contentDescription = "Icon 1",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onPickImage()
                        },
                    tint = onPrimaryColor
                )

                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Icon 2",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onPickDate()



                        },
                    tint = onPrimaryColor

                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_time),
                    contentDescription = "Icon 3",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onPickTime()

                        },
                    tint = onPrimaryColor

                )
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Icon 4",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            // ADD_NOTE

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                onRequestPermission()
                            } else {
                                onInsertNote(context)
                            }
                            // onRequestPermission()

                        },
                    tint = onPrimaryColor

                )
            }


        }

    )
}









@Composable
fun NotificationRow(
    date: String,
    time: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = time,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }


    }
}

