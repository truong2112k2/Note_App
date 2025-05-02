package com.example.noteapp.domain.use_case

import android.net.Uri
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
class AddUseCase @Inject constructor(
    private var noteRepository: INoteRepository,
    private var imageRepository: IImageRepository
) {
    suspend fun insertNote(note: Note): Long {
        return withContext(Dispatchers.IO) {
            try {

                val noteConvert = note.toNoteEntity()
                noteRepository.insertNote(noteConvert)

            } catch (e: Exception) {
                Log.d(Constants.ERROR, "AddUseCase insertNote ${e.message}")
                -1
            }
        }

    }

    suspend fun insertImageToFileDir(uri: Uri, fileName: String): String {
        return withContext(Dispatchers.Default) {
            try {
                imageRepository.saveImage(uri, fileName)

            } catch (e: Exception) {
                Log.d(Constants.ERROR, "AddUseCase insertImageToFileDir ${e.message}")
                ""
            }

        }

    }


}