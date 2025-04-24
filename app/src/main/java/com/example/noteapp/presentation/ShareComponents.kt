package com.example.noteapp.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CustomDatePicker(
    showPickerDate : Boolean,
    onClickPositiveButton: () -> Unit,
    onClickNegativeButton: () -> Unit,
    onSelectDate: (localDate: String) -> Unit
) {
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
            positiveButton("OK") {
                onClickPositiveButton()
            }
            negativeButton("Cancel") {
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
                title = "Pick Date"
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

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    AlertDialogTimePicker(
        onDismiss = { onDismiss() },
        onConfirm = { onConfirm(timePickerState) }
    ) {
        TimePicker(
            state = timePickerState,
        )
    }
}

