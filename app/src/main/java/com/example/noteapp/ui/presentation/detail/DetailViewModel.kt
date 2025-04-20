package com.example.noteapp.ui.presentation.detail

import android.annotation.SuppressLint
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
import com.example.noteapp.domain.use_case.GetNoteUseCase
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
    private val addNoteUseCase: AddNoteUseCase
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

    // fun updateNote(note: NoteEntity): Int



    private val _detailState = MutableStateFlow<DetailState>(DetailState())
    val detailState : StateFlow<DetailState> = _detailState.asStateFlow()

    @SuppressLint("NewApi")
    fun updateNoteDatabase(note: Note){

        _detailState.value = DetailState(isLoading = true)

        viewModelScope.launch(Dispatchers.IO) {

            val currentTime = LocalDateTime.now()
            val today = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)
            val currentDate = today.format(formatter) // thời gian thêm note


            val originalNote = getNotesUseCase.getNoteById(note.id)
            Log.d(Constants.STATUS_TAG_DETAIL_SCREEN,"originalNote value = ${originalNote.toString()}")





            if(selectedImageUri.value != null){
                if(note.image!!.isNotEmpty()){

                    val delete = updateNoteUseCase.deleteImage(note.image.toString())

                    if( delete){

                        val imagePath = if (selectedImageUri.value != null) {
                            addNoteUseCase.saveImageToFileDir(
                                selectedImageUri.value!!,
                                currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ".Jpg"

                            )

                        } else {
                            selectedImageUri.value.toString()
                        }
                        Log.d(Constants.STATUS_TAG_DETAIL_SCREEN,"Image name value = ${currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ".Jpg"}")


                        val newNote = note.copy(
                            image = imagePath,
                            dateAdd = currentDate
                        )


                        if(newNote.title.isEmpty() || newNote.content.isEmpty()){
                            _detailState.value = DetailState(isLoading = false, error = "ERROR: Fill in all fields. Please!")

                        }else{
                            updateNoteUseCase.updateNote(newNote)

                        }

                        _detailState.value = DetailState(isLoading = false, isSuccess = true )

                        Log.d(Constants.STATUS_TAG_DETAIL_SCREEN,"Update Note have Image_Delete_Image, new note value = ${note.toString()}")

                    } else{
                        _detailState.value = DetailState(isLoading = false, error = "ERROR: Can't Update Note")


                    }
                }
                else{
                    val imagePath = if (selectedImageUri.value != null) {
                        addNoteUseCase.saveImageToFileDir(
                            selectedImageUri.value!!,
                            currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ".Jpg")

                    } else {
                        selectedImageUri.value.toString()
                    }
                    Log.d(Constants.STATUS_TAG_DETAIL_SCREEN,"Image name value = ${currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + ".Jpg"}")


                    val newNote = note.copy(
                        image = imagePath,
                        dateAdd = currentDate
                    )

                    if(newNote.title.isEmpty() || newNote.content.isEmpty()){
                        _detailState.value = DetailState(isLoading = false, error = "ERROR: Fill in all fields. Please!")

                    }else{
                        updateNoteUseCase.updateNote(newNote)

                    }

                    _detailState.value = DetailState(isLoading = false, isSuccess = true )

                    Log.d(Constants.STATUS_TAG_DETAIL_SCREEN,"Update Note have Image, new note value = ${newNote.toString()}")
                }
            }else{
                val newNote = note.copy(
                    dateAdd = currentDate
                )
                if(originalNote!!.equals(newNote)){

                    _detailState.value = DetailState(isLoading = false, error = "ERROR: Nothing Change" )

                    Log.d(Constants.STATUS_TAG_DETAIL_SCREEN,"Khong co gia tri nao thay doi?")
                }else{
                    if(newNote.title.isEmpty() || newNote.content.isEmpty()){
                        _detailState.value = DetailState(isLoading = false, error = "ERROR: Fill in all fields. Please!")

                    }else{
                        updateNoteUseCase.updateNote(newNote)
                        _detailState.value = DetailState(isLoading = false, isSuccess = true )


                    }

                }
                Log.d(Constants.STATUS_TAG_DETAIL_SCREEN,"Update Note don't have Image, new note value = ${newNote.toString()}")

            }



        }

    }



    //------------------------------------------------------------------///

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


    var showPickerDate = mutableStateOf(false)
    var selectedDate = mutableStateOf("00/00/0000")//reset

    var selectedImageUri = mutableStateOf<Uri?>(null) // reset




    var showDialog = mutableStateOf(false)

    var dialogMessage =  mutableStateOf("") //reset

    fun updateShowDialog(updateValue: Boolean){
        showDialog.value = updateValue
    }

    fun updateDialogMessage(updateValue: String){
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



    fun updateSelectedDate(updateValue: String) {
        selectedDate.value = updateValue
    }

    fun updateSelectedImageUri(updateValue: Uri?) {
        selectedImageUri.value = updateValue

    }



}



