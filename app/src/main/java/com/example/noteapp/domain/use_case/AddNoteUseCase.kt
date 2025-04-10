package com.example.noteapp.domain.use_case

import android.net.Uri
import android.util.Log
import com.example.noteapp.common.Constants
import com.example.noteapp.common.toNoteEntity
import com.example.noteapp.data.entity.NoteEntity
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.IImageRepository
import com.example.noteapp.domain.repository.INoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddNoteUseCase @Inject constructor(
    private var noteRepository: INoteRepository,
    private var imageRepository: IImageRepository
) {
     suspend fun insertNote(note: Note): Long {
         Log.d(Constants.STATUS_TAG,"Insert in NoteUseCase")

         return withContext(Dispatchers.IO){
            try {
                val noteConvert = note.toNoteEntity()

                noteRepository.insertNote(noteConvert)

            }catch (e: Exception){
                Log.d(Constants.ERROR_TAG,"ERROR INSERT NOTE ${e.message}")
                -1
            }
        }
    }

    suspend fun saveImageToFileDir(uri: Uri, fileName: String): String{
      return  imageRepository.saveImage(uri, fileName)
    }


}