package com.example.noteapp.presentation.detail

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
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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


    private val _updateSate = MutableStateFlow<UpdateState>(UpdateState())
    val updateState: StateFlow<UpdateState> = _updateSate.asStateFlow()



    @SuppressLint("NewApi")
    fun updateNoteDatabase(note: Note, context: Context) {
        _updateSate.value = UpdateState(isLoading = true)

        viewModelScope.launch(Dispatchers.IO) {
            val currentTime = LocalDateTime.now()
            val today = LocalDate.now()
        //    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            val currentDate = today.format(formatter)

            val originalNote = getNotesUseCase.getNoteById(note.id)
            var newImagePath: String? = note.image

            selectedImageUri.value?.let { uri ->
                // Nếu có ảnh mới được chọn
                if (!note.image.isNullOrEmpty()) {
                    val deleted = updateNoteUseCase.deleteImage(note.image.toString())
                    if (!deleted) {
                        _updateSate.value = UpdateState(error = "ERROR: Can't Update Note")
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
                _updateSate.value =
                    UpdateState( error = "ERROR: Fill in all fields. Please!")
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
                    _updateSate.value =
                        UpdateState(error = "ERROR: Nothing Change")
                    return@launch
                }
            }

            updateNoteUseCase.updateNote(newNote)
            Log.d("2312321", "ID note Update ${note.id}")
            scheduleNotifyUseCase.scheduleNotification(context, note, note.id.toString())
            _updateSate.value = UpdateState(isLoading = false, isSuccess = true)

            Log.d(Constants.STATUS_TAG_DETAIL_SCREEN, "Update completed: ${newNote}")
        }
    }


    //------------------------------------------------------------------///


    val showDiaLogDelete = mutableStateOf(false)

    fun setShowDialogDelete(updateValue: Boolean){
        showDiaLogDelete.value = updateValue
    }

    fun deleteNoteById(context: Context,note: Note , onSuccess : () -> Unit  ) {
        viewModelScope.launch(Dispatchers.IO) {

            runCatching {
                val delete = deleteNoteUseCase.deleteNoteById(note.id)
                if (delete == -1) throw Exception("Delete failed in DB")

                // Nếu có ảnh, xóa ảnh trong Firebase Storage
                note.image?.let {
                    updateNoteUseCase.deleteImage(it)
                }

                // Hủy notification
                scheduleNotifyUseCase.cancelNoteNotification(
                    context = context,
                    noteId = note.id.toString()
                )

            }.onSuccess {
                withContext(Dispatchers.Main){
                    onSuccess()
                }

                Log.d(Constants.STATUS_TAG_DETAIL_SCREEN, "Delete successfully")
            }.onFailure { e ->

                   showDialogUpdate.value = true

                Log.e(Constants.STATUS_TAG_DETAIL_SCREEN, "Delete failed: ${e.message}")
            }



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


    var showDialogUpdate = mutableStateOf(false)

    var dialogUpdateMessage = mutableStateOf("") //reset

    fun setShowDialogUpdate(updateValue: Boolean) {
        showDialogUpdate.value = updateValue
    }

    fun setDialogUpdateMessage(updateValue: String) {
        dialogUpdateMessage.value = updateValue
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



