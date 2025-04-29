package com.example.noteapp.presentation.add_note.viewmodel

import com.example.noteapp.common.TimeUtils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.common.Constants
import com.example.noteapp.domain.model.ItemDropMenu
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val addNoteFields = AddScreenUIFields()

    val titleNote = addNoteFields.titleNote
    val contentNote = addNoteFields.contentNote
    val categoryMenuExpanded = addNoteFields.categoryMenuExpanded
    val selectedCategory = addNoteFields.selectedCategory
    val priorityMenuExpanded = addNoteFields.priorityMenuExpanded
    val selectedPriority = addNoteFields.selectedPriority
    val showPickerTime = addNoteFields.showPickerTime
    val selectedTime = addNoteFields.selectedTime
    val showPickerDate = addNoteFields.showPickerDate
    val selectedDate = addNoteFields.selectedDate

    val selectedImageUri = addNoteFields.selectedImageUri

    val showDialog = addNoteFields.showDialog
    val dialogMessage = addNoteFields.dialogMessage

    val listCategory = addNoteFields.listCategory
    val listPriority = addNoteFields.listPriority

    // Hàm để cập nhật tiêu đề ghi chú
    fun updateTitleNote(newTitle: String) {
        titleNote.value = newTitle
    }

    // Hàm để cập nhật nội dung ghi chú
    fun updateContentNote(newContent: String) {
        contentNote.value = newContent
    }

    // Hàm để cập nhật trạng thái mở/đóng menu category
    fun updateCategoryMenuExpanded(isExpanded: Boolean) {
        categoryMenuExpanded.value = isExpanded
    }

    // Hàm để cập nhật category đã chọn
    fun updateSelectedCategory(newCategory: ItemDropMenu) {
        selectedCategory.value = newCategory
    }

    // Hàm để cập nhật trạng thái mở/đóng menu priority
    fun updatePriorityMenuExpanded(isExpanded: Boolean) {
        priorityMenuExpanded.value = isExpanded
    }

    // Hàm để cập nhật priority đã chọn
    fun updateSelectedPriority(newPriority: ItemDropMenu) {
        selectedPriority.value = newPriority
    }

    // Hàm để cập nhật trạng thái hiển thị picker thời gian
    fun updateShowPickerTime(isVisible: Boolean) {
        showPickerTime.value = isVisible
    }

    // Hàm để cập nhật thời gian đã chọn
    fun updateSelectedTime(newTime: String) {
        selectedTime.value = newTime
    }

    // Hàm để cập nhật trạng thái hiển thị picker ngày
    fun updateShowPickerDate(isVisible: Boolean) {
        showPickerDate.value = isVisible
    }

    // Hàm để cập nhật ngày đã chọn
    fun updateSelectedDate(newDate: String) {
        selectedDate.value = newDate
    }

    // Hàm để cập nhật URI của hình ảnh đã chọn
    fun updateSelectedImageUri(uri: Uri?) {
        selectedImageUri.value = uri
    }

    // Hàm để hiển thị/hide dialog
    fun updateShowDialog(isVisible: Boolean) {
        showDialog.value = isVisible
    }

    // Hàm để cập nhật thông báo trong dialog
    fun updateDialogMessage(message: String) {
        dialogMessage.value = message
    }



    // Hàm để reset tất cả các trường
    fun resetFields() {
        addNoteFields.resetFields()
    }


    // UI state cho việc insert note (loading, success, error)
    private var _addNoteState = mutableStateOf(AddNoteState())
    val addNoteState: State<AddNoteState> = _addNoteState


        fun resetAddState() {
        _addNoteState.value = AddNoteState()
    }
    @SuppressLint("NewApi")
    fun insertNote(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _addNoteState.value = AddNoteState(isLoading = true)

            try {
                // Kiểm tra các trường thông tin
                if (selectedCategory.value.nameOrId == "Category" ||
                    selectedPriority.value.nameOrId== "Priority" ||
                    titleNote.value.isEmpty() ||
                    contentNote.value.isEmpty())
                {
                    _addNoteState.value = AddNoteState(error = "Fill in all fields. Please!")
                    Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, _addNoteState.value.error)
                    return@launch
                }

                if (selectedTime.value == "00:00" || selectedDate.value == "00/00/0000") {
                    _addNoteState.value = AddNoteState(error = "Select time and date. Please!")
                    Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, _addNoteState.value.error)
                    return@launch
                }

                if (titleNote.value.length > 50 || contentNote.value.length > 5000) {
                    _addNoteState.value = AddNoteState(error = "Title or content too long")
                    Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, _addNoteState.value.error)
                    return@launch
                }

                if (TimeUtils.isNotifyTimeInPast(selectedDate.value, selectedTime.value)) {
                    _addNoteState.value = AddNoteState(error = "Time is in the past")
                    Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, _addNoteState.value.error)
                    return@launch
                }

                // Tạo thời gian hiện tại
                val currentTime = LocalDateTime.now()
                val today = LocalDate.now()
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val currentDate = today.format(formatter)



                val imagePath = if (selectedImageUri.value != null) {
                    noteUseCases.addUseCase.insertImageToFileDir(
                        selectedImageUri.value!!,
                        currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ".Jpg"
                    )
                } else {
                    selectedImageUri.value.toString()
                }


                val note = Note(
                    title = titleNote.value,
                    content = contentNote.value,
                    dateAdd = currentDate,
                    category = selectedCategory.value.nameOrId,
                    priority = selectedPriority.value.nameOrId.toInt(),
                    image = imagePath,
                    timeNotify = selectedTime.value,
                    dateNotify = selectedDate.value
                )

                // Insert note vào DB
                val idNoteInsert = noteUseCases.addUseCase.insertNote(note)

                if (idNoteInsert.toInt() == -1) {
                    _addNoteState.value = AddNoteState(error = "Insert failed")
                    Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, _addNoteState.value.error)
                } else {
                    _addNoteState.value = AddNoteState(isSuccess = true)

                    Log.d("add", _addNoteState.value.isSuccess.toString())
                    noteUseCases.scheduleUseCase.scheduleNotification(context, note, idNoteInsert.toString())
                    Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, _addNoteState.value.isSuccess.toString())
                }

            } catch (e: Exception) {
                _addNoteState.value = AddNoteState(error = e.message ?: "Unknown error")
                Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, e.message.toString())
            }
        }
    }
}