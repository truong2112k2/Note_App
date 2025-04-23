package com.example.noteapp.ui.presentation.detail

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.R
import com.example.noteapp.common.Constants
import com.example.noteapp.domain.model.ItemDropMenu
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.use_case.AddNoteUseCase
import com.example.noteapp.domain.use_case.DeleteNoteUseCase
import com.example.noteapp.domain.use_case.GetNoteUseCase
import com.example.noteapp.domain.use_case.ScheduleNotifyUseCase
import com.example.noteapp.domain.use_case.UpdateNoteUseCase
import com.example.noteapp.ui.theme.high
import com.example.noteapp.ui.theme.low
import com.example.noteapp.ui.theme.medium
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getNotesUseCase: GetNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val scheduleNotifyUseCase: ScheduleNotifyUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase

) : ViewModel() {

    val switchTopAppBar = mutableStateOf(false)

    fun updateSwitchTopAppBar(updateValue: Boolean) {
        switchTopAppBar.value = updateValue
    }


    private val defaultNote = Note(0, "null", "null", "null", "null", 0, "null", "null", "null")
    private val _note = MutableStateFlow<Note>(defaultNote)
    val note: StateFlow<Note> = _note


    fun getNoteById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val noteDetail = getNotesUseCase.getNoteById(id)
            if (noteDetail != null) {
                _note.value = noteDetail
            }
        }

    }

    fun updateNote(updateValue: Note) {
        _note.value = updateValue
    }


    private val _detailState = MutableStateFlow<DetailState>(DetailState())
    val detailState: StateFlow<DetailState> = _detailState.asStateFlow()


    @SuppressLint("NewApi")
    fun updateNoteDatabase(note: Note, context: Context) {
        _detailState.value = DetailState(isLoading = true)

        viewModelScope.launch(Dispatchers.IO) {
            val currentTime = LocalDateTime.now()
            val today = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
            val currentDate = today.format(formatter)

            val originalNote = getNotesUseCase.getNoteById(note.id)
            var newImagePath: String? = note.image

            selectedImageUri.value?.let { uri ->
                // Nếu có ảnh mới được chọn
                if (!note.image.isNullOrEmpty()) {
                    val deleted = updateNoteUseCase.deleteImage(note.image.toString())
                    if (!deleted) {
                        _detailState.value = DetailState(error = "ERROR: Can't Update Note")
                        return@launch
                    }
                }

                newImagePath = addNoteUseCase.saveImageToFileDir(
                    uri,
                    currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ".Jpg"
                )
                Log.d(Constants.STATUS_TAG_DETAIL_SCREEN, "Saved new image: $newImagePath")
            }

            if(showImage.value == false){
              updateNoteUseCase.deleteImage(note.image.toString())
                newImagePath = null
            }
            val newNote = note.copy(
                image = newImagePath,
                dateAdd = currentDate
            )

            if (newNote.title.isEmpty() || newNote.content.isEmpty()) {
                _detailState.value = DetailState( error = "ERROR: Fill in all fields. Please!")
                return@launch
            }



            if (originalNote != null) {
                if (
                    selectedImageUri.value == null &&
                    originalNote.title == newNote.title &&
                    originalNote.content == newNote.content &&
                    originalNote.category == newNote.category &&
                    originalNote.priority == newNote.priority &&
                    originalNote.image == newNote.image &&
                    originalNote.dateNotify == newNote.dateNotify &&
                    originalNote.timeNotify == newNote.timeNotify
                ) {
                    _detailState.value =
                        DetailState(error = "ERROR: Nothing Change")
                    return@launch
                }
            }

            updateNoteUseCase.updateNote(newNote)
            scheduleNotifyUseCase.scheduleNotification(context, note)
            _detailState.value = DetailState(isLoading = false, isSuccess = true)

            Log.d(Constants.STATUS_TAG_DETAIL_SCREEN, "Update completed: ${newNote}")
        }
    }


    //------------------------------------------------------------------///

    fun deleteNoteById(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
           val delete = deleteNoteUseCase.deleteNoteById(id)

        }
    }

    private val defaultItemCategory = ItemDropMenu("Category", icon = R.drawable.ic_category)

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


    var showPickerDate = mutableStateOf(false)

    var selectedImageUri = mutableStateOf<Uri?>(null) // reset


    var showDialog = mutableStateOf(false)

    var dialogMessage = mutableStateOf("") //reset

    fun updateShowDialog(updateValue: Boolean) {
        showDialog.value = updateValue
    }

    fun updateDialogMessage(updateValue: String) {
        dialogMessage.value = updateValue
    }


    fun updateCategoryMenuExpended(updateValue: Boolean) {
        categoryMenuExpanded.value = updateValue

    }


    fun updatePriorityMenuExpanded(updateValue: Boolean) {
        priorityMenuExpanded.value = updateValue
    }

    fun updateShowPickerTime(updateValue: Boolean) {
        showPickerTime.value = updateValue
    }

    fun updateShowPickerDate(updateValue: Boolean) {
        showPickerDate.value = updateValue
    }


    fun updateSelectedImageUri(updateValue: Uri?) {
        selectedImageUri.value = updateValue

    }


    val showImage = mutableStateOf(true)

    fun updateShowImage(updateValue: Boolean){
        showImage.value = updateValue
    }


}



