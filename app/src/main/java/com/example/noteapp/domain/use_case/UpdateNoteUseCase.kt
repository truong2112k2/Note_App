package com.example.noteapp.domain.use_case

import android.util.Log
import com.example.noteapp.common.Constants
import com.example.noteapp.data.toNoteEntity
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.IImageRepository
import com.example.noteapp.domain.repository.INoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton

class UpdateNoteUseCase @Inject constructor(
    private var noteRepository: INoteRepository,
    private var imageRepository: IImageRepository

    ) {

    suspend fun updateNote(note: Note): Int {
        return withContext(Dispatchers.IO){
            try {
                val noteConvert = note.toNoteEntity()
                noteRepository.updateNote(noteConvert)
            }catch (e: Exception) {
                Log.d(Constants.ERROR_TAG_DETAIL_SCREEN, "ERROR UPDATE NOTE ${e.message}")
                -1
            }
        }
    }

    suspend fun deleteImage(fileName: String): Boolean{
        return withContext(Dispatchers.Default){
            try{
                imageRepository.deleteImage(fileName)
            }catch ( e: Exception){
                Log.d(Constants.ERROR_TAG_DETAIL_SCREEN, "ERROR DELETE IMAGE ${e.message}")
                false
            }
        }

    }
}