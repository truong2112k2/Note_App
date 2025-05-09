package com.example.noteapp.presentation.detail.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.common.Constants
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.use_case.NoteUseCases
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
    private val noteUseCases: NoteUseCases

) : ViewModel() {

    private val detailNoteFields = DetailScreenFields()

    val isShowAllDetail =  detailNoteFields.isShowAllDetail

    val switchTopAppBar = detailNoteFields.switchTopAppBar

    val showDiaLogDelete =detailNoteFields.showDiaLogDelete

    val listCategory = detailNoteFields.listCategory

    val listPriority = detailNoteFields.listPriority

    var categoryMenuExpanded = detailNoteFields.categoryMenuExpanded
    var priorityMenuExpanded = detailNoteFields.priorityMenuExpanded
    var showPickerTime = detailNoteFields.showPickerTime
    var showPickerDate = detailNoteFields.showPickerDate
    var selectedImageUri = detailNoteFields.selectedImageUri
    var showDialogUpdate = detailNoteFields.showDialogUpdate
    var dialogUpdateMessage = detailNoteFields.dialogUpdateMessage
    val showImage = detailNoteFields.showImage



    private val defaultNote = Note(0, "null", "null", "null", "null", 0, "null", "null", "null")
    private val _note = MutableStateFlow<Note>(defaultNote)
    val note: StateFlow<Note> = _note


    fun getNoteById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val noteDetail =  noteUseCases.getUseCase.getNoteById(id)
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
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val currentDate = today.format(formatter)

            val originalNote = noteUseCases.getUseCase.getNoteById(note.id)



            var newImagePath: String? = note.image

            val hasNewImage = selectedImageUri.value != null

//  Xử lý ảnh mới được chọn
            if (hasNewImage) {
                val uri = selectedImageUri.value!!

                // Nếu ghi chú đã có ảnh → xóa ảnh cũ
                if (originalNote?.image != "null") {
                //    val deleted = updateNoteUseCase.deleteImage(originalNote?.image.toString())
                    val deleted = noteUseCases.deleteUseCase.deleteImage(originalNote!!.image!!)

                    if (!deleted) {
                        _updateSate.value = UpdateState(error = "ERROR: Can't delete old image")
                        return@launch
                    }
                }

                // Lưu ảnh mới vào file
                newImagePath = noteUseCases.addUseCase.insertImageToFileDir(
                    uri,
                    currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ".Jpg"
                )


            }


            //  Nếu người dùng chọn ẩn ảnh (bỏ ảnh)
            if (showImage.value == false) {
                noteUseCases.deleteUseCase.deleteImage(note.image.toString())
                newImagePath = null
            }

            //  Tạo bản ghi chú mới đã được chỉnh sửa
            val newNote = note.copy(
                image = newImagePath,
                dateAdd = currentDate
            )

            //  Kiểm tra các trường bắt buộc
            if (newNote.title.isEmpty() || newNote.content.isEmpty()) {
                _updateSate.value = UpdateState(error = "ERROR: Fill in all fields. Please!")
                return@launch
            }

            //  Kiểm tra nếu không có thay đổi gì
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
                    _updateSate.value = UpdateState(error = "ERROR: Nothing Change")
                    return@launch
                }
            }

            // 👉 Cập nhật ghi chú
            noteUseCases.updateUseCase.updateNote(newNote)

            // 👉 Lên lịch thông báo nếu có
            noteUseCases.scheduleUseCase.scheduleNotification(context, note, note.id.toString())

            _updateSate.value = UpdateState(isLoading = false, isSuccess = true)
            Log.d(Constants.STATUS, "DetailViewModel-updateNoteDatabase: Update completed: $newNote")

        }
    }





    fun setShowDialogDelete(updateValue: Boolean){
        showDiaLogDelete.value = updateValue
    }

    fun deleteNoteById(context: Context,note: Note , onSuccess : () -> Unit  ) {
        viewModelScope.launch(Dispatchers.IO) {

            runCatching {
                val delete = noteUseCases.deleteUseCase.deleteNoteById(note.id)
                if (delete == -1) throw Exception("Delete failed in DB")

                // Nếu có ảnh, xóa ảnh trong Firebase Storage
                note.image?.let {
                    noteUseCases.deleteUseCase.deleteImage(it)
                }

                // Hủy notification
                noteUseCases.scheduleUseCase.cancelScheduleNotification(
                    context = context,
                    noteId = note.id.toString()
                )

            }.onSuccess {
                withContext(Dispatchers.Main){
                    onSuccess()
                }

                Log.d(Constants.STATUS, "DetailViewModel deleteNoteById: Delete is successfully")
            }.onFailure { e ->

                   showDialogUpdate.value = true

                Log.e(Constants.ERROR, "DetailViewModel deleteNoteById:Delete failed: ${e.message}")
            }



        }
    }








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



    fun updateShowImage(updateValue: Boolean){
        showImage.value = updateValue
    }

    fun updateIsShowAllDetail(updateValue: Boolean){

        isShowAllDetail.value = updateValue
    }


    fun updateSwitchTopAppBar(updateValue: Boolean) {
        switchTopAppBar.value = updateValue
    }

}



