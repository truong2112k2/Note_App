package com.example.noteapp.presentation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun AlertDialogTimePicker(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = {
            content()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomDatePicker(
    showPickerDate : Boolean,
    onClickPositiveButton: () -> Unit,
    onClickNegativeButton: () -> Unit,
    onSelectDate: (localDate: String) -> Unit
) {

    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val dateDialogState = rememberMaterialDialogState()

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val transition = updateTransition(targetState = showPickerDate, label = "Dialog Transition")

// Tạo hiệu ứng fade, slide cho dialog
    val alpha by transition.animateFloat(label = "Alpha") { state ->
        if (state) 1f else 0f
    }

    val slideY by transition.animateDp(label = "Slide Y") { state ->
        if (state) 0.dp else 100.dp // Slide lên trên khi show, xuống dưới khi ẩn
    }

    if (showPickerDate) {
        dateDialogState.show()
    }

    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton("OK", textStyle = TextStyle(color = primaryColor)) {
                onClickPositiveButton()
            }
            negativeButton("Cancel", textStyle = TextStyle(color = primaryColor)) {
                onClickNegativeButton()
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        // Sử dụng LocalDensity để chuyển Dp thành Px
        val slideYPx = with(LocalDensity.current) { slideY.toPx() }  // Đúng cách chuyển Dp thành Px

        Box(
            modifier = Modifier
                .graphicsLayer(
                    alpha = alpha, // Điều chỉnh độ trong suốt
                    translationY = slideYPx // Điều chỉnh độ trượt theo trục Y
                )
        ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "Pick Date",
             colors = com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults.colors(
                 headerBackgroundColor = onPrimaryColor,
                 headerTextColor = primaryColor,
                 calendarHeaderTextColor = primaryColor,
                 dateActiveBackgroundColor = Color.Black,
                 dateActiveTextColor = Color.Yellow,
                 dateInactiveTextColor = Color.DarkGray,
             )



            ) {
                onSelectDate(it.format(formatter))
            }
        }
    }



}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePicker(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )



        val customColors = TimePickerDefaults.colors(
        clockDialColor = primaryColor, // Color of the clock dial
        clockDialSelectedContentColor = Color.Black, // Selected content color on the dial
        clockDialUnselectedContentColor = onPrimaryColor, // Unselected content color on the dial
        selectorColor = Color.White, // Color for the selector (hour/minute)
        containerColor = primaryColor, // Container color for the TimePicker
        timeSelectorSelectedContainerColor = onPrimaryColor, // Background color for selected time
        timeSelectorUnselectedContainerColor = onPrimaryColor, // Background color for unselected time
        timeSelectorSelectedContentColor = primaryColor, // Text color for selected time
        timeSelectorUnselectedContentColor = primaryColor, // Text color for unselected time
    )
    // Define custom colors for the buttons
    val confirmButtonColors = ButtonDefaults.buttonColors(
        containerColor = onPrimaryColor, // Background color for Confirm button
        contentColor = primaryColor // Text color for Confirm button
    )

    val dismissButtonColors = ButtonDefaults.buttonColors(
        containerColor = onPrimaryColor, // Background color for Confirm button
        contentColor = primaryColor // Text color for Confirm button
    )

    // Create an AlertDialog with a custom Confirm and Dismiss button
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Select Time") },
        text = {
            TimePicker(
                state = timePickerState,
                colors = customColors
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(timePickerState) },
                colors = confirmButtonColors // Apply custom colors to the Confirm button
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() },
                colors = dismissButtonColors // Apply custom colors to the Dismiss button
            ) {
                Text("Dismiss")
            }
        }
    )
}


@Composable
fun ConfirmDeleteDialog(
    title: String,
    message: String,
    positiveText: String,
    negativeText: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimary)
        },
        text = {
            Text(text = message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(positiveText, color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(negativeText, color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun CreateATitle(title: String){
    Text(title , style = MaterialTheme.typography.displayMedium, color = MaterialTheme.colorScheme.onPrimary)

}