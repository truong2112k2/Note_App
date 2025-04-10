package com.example.noteapp.ui.presentation.add_note

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.noteapp.R
import com.example.noteapp.WorkManager.NotificationWorker
import com.example.noteapp.common.Constants
import com.example.noteapp.data.preferences.NotePreferences
import com.example.noteapp.domain.model.ItemDropMenu
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.use_case.AddNoteUseCase
import com.example.noteapp.ui.theme.high
import com.example.noteapp.ui.theme.low
import com.example.noteapp.ui.theme.medium
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val noteUseCase: AddNoteUseCase
) : ViewModel() {


    val titleNote = mutableStateOf("") //reset

    val contentNote = mutableStateOf("")//reset

    private val defaultItemCategory = ItemDropMenu("Category", icon =  R.drawable.ic_category )

    val listCategory = listOf(
        ItemDropMenu("Work", icon = R.drawable.ic_work),
        ItemDropMenu("Relax", icon = R.drawable.ic_relax),
        ItemDropMenu("Sport", icon = R.drawable.ic_sport),
        ItemDropMenu("Language", icon = R.drawable.ic_language),
        ItemDropMenu("Else", icon = R.drawable.ic_else),
    )

    var categoryMenuExpanded = mutableStateOf(false)

    var selectedCategory = mutableStateOf(defaultItemCategory)//reset



    private val defaultItemPriority = ItemDropMenu("Priority", color = Color.Transparent)
    val listPriority = listOf(
        ItemDropMenu("1", color = low),
        ItemDropMenu("2", color = medium),
        ItemDropMenu("3", color = high),
    )
    var priorityMenuExpanded = mutableStateOf(false)
    var selectedPriority = mutableStateOf(defaultItemPriority)//reset


    var showPickerTime = mutableStateOf(false)
    var selectedTime = mutableStateOf("00:00")//reset


    var showPickerDate = mutableStateOf(false)
    var selectedDate = mutableStateOf("00/00/0000")//reset

    var selectedImageUri = mutableStateOf<Uri?>(null) // reset

    var showDialog = mutableStateOf(false)

    var dialogMessage =  mutableStateOf("") //reset

    private val _addNoteState = mutableStateOf(AddNoteState())
    val addNoteState: State<AddNoteState> = _addNoteState


    fun updateTitleNote(updateValue: String) {
        titleNote.value = updateValue
    }

    fun updateContentNote(updateValue: String) {
        contentNote.value = updateValue
    }

    fun updateCategoryMenuExpended(updateValue: Boolean) {
        categoryMenuExpanded.value = updateValue

    }

    fun updateSelectCategory(updateValue: ItemDropMenu) {
        selectedCategory.value = updateValue
    }

    fun updatePriorityMenuExpanded(updateValue: Boolean) {
        priorityMenuExpanded.value = updateValue
    }

    fun updateSelectPriority(updateValue: ItemDropMenu) {
        selectedPriority.value = updateValue
    }

    fun updateShowPickerTime(updateValue: Boolean) {
        showPickerTime.value = updateValue
    }

    fun updateShowPickerDate(updateValue: Boolean) {
        showPickerDate.value = updateValue
    }

    fun updateSelectedTime(updateValue: String) {
        selectedTime.value = updateValue
    }

    fun updateSelectedDate(updateValue: String) {
        selectedDate.value = updateValue
    }

    fun updateSelectedImageUri(updateValue: Uri?) {
        selectedImageUri.value = updateValue

    }

    fun updateShowDialog(updateValue: Boolean){
        showDialog.value = updateValue
    }

    fun updateDialogMessage(updateValue: String){
        dialogMessage.value = updateValue
    }

    fun resetAddState() {
        _addNoteState.value = AddNoteState()
    }

    fun resetAddNoteField(){
        titleNote.value = ""
        contentNote.value =""
        selectedCategory.value = defaultItemCategory
        selectedPriority.value = defaultItemPriority
        selectedTime.value ="00:00"
        selectedDate.value = "00/00/0000"
        selectedImageUri.value = null
        dialogMessage.value = ""
    }

    @SuppressLint("NewApi", "SuspiciousIndentation")
    fun insertNote() {
        viewModelScope.launch(Dispatchers.IO) {
            _addNoteState.value = AddNoteState(isLoading = true)
            try {

                if (selectedTime.value.isEmpty() || selectedDate.value.isEmpty() || selectedCategory.value == defaultItemCategory || selectedPriority.value == defaultItemPriority || titleNote.value.isEmpty() || contentNote.value.isEmpty()) {
                    _addNoteState.value = AddNoteState(error = "Please fill in all fields.")
                    Log.d(Constants.ERROR_TAG, _addNoteState.value.error)
                    return@launch
                }

                if (titleNote.value.length > 50 || contentNote.value.length > 5000) {
                    _addNoteState.value = AddNoteState(error = "Title or content too long")
                    Log.d(Constants.ERROR_TAG, _addNoteState.value.error)

                    return@launch

                }

                val currentTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                val currentDate = currentTime.format(formatter)


                val imagePath = if (selectedImageUri.value != null) {
                    noteUseCase.saveImageToFileDir(
                        selectedImageUri.value!!,
                        currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ".Jpg"
                    )
                } else {
                    selectedImageUri.value.toString()
                }

                val note = Note(
                    title = titleNote.value,
                    content = contentNote.value,
                    time = currentDate,
                    category = selectedCategory.value.nameOrId,
                    priority = selectedPriority.value.nameOrId.toInt(),
                    image = imagePath,
                    timeNotify = selectedTime.value,
                    date = selectedDate.value
                )
                val insertNote = noteUseCase.insertNote(note)
                if (insertNote.toInt() == -1) {
                    _addNoteState.value = AddNoteState(error = "Insert failed")
                    Log.d(Constants.ERROR_TAG, _addNoteState.value.error)

                } else {
                    _addNoteState.value = AddNoteState(isSuccess = true)
                    Log.d(Constants.ERROR_TAG, _addNoteState.value.isSuccess.toString())

                }

            } catch (e: Exception) {
                Log.d(Constants.ERROR_TAG, e.message.toString())
                _addNoteState.value = AddNoteState(error = e.message ?: "Unknown error")

            }

        }


    }


    private val _resultSaveTime = MutableStateFlow("")
    val resultSaveTime: StateFlow<String> = _resultSaveTime.asStateFlow()

    fun scheduleNotification(context: Context, hour: Int, minute: Int, workManager: WorkManager) {
        val nowTime = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        val time = targetTime.timeInMillis - nowTime.timeInMillis
        if (time <= 0) {
            return
        }

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(time, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueue(workRequest)
    }


    fun saveTimeToPreferences(context: Context, hour: Int, minute: Int) {

        viewModelScope.launch {
            val saveTime = NotePreferences.saveTimeToPreferences(context, hour, minute)
            if (saveTime) {
                _resultSaveTime.value = "Save time success"
            } else {
                _resultSaveTime.value = "Save time failure"

            }
        }
    }

    // Lấy thời gian đã lưu từ SharedPreferences
    fun getSavedTimeFromPreferences(context: Context) {
        viewModelScope.launch {
            NotePreferences.getSavedTimeFromPreferences(context) // xử lý sau dj
        }
    }

}