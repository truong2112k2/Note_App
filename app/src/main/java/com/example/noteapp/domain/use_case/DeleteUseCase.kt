package com.example.noteapp.domain.use_case

import android.util.Log
import com.example.noteapp.common.Constants
import com.example.noteapp.domain.repository.IImageRepository
import com.example.noteapp.domain.repository.INoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton

class DeleteUseCase @Inject constructor(
    private var noteRepository: INoteRepository,
    private var imageRepository: IImageRepository

) {
    suspend fun deleteNoteById(id: Long): Int {
        return withContext(Dispatchers.IO) {
            try {
                noteRepository.deleteNoteById(id)

            } catch (e: Exception) {
                Log.d(Constants.ERROR, "DeleteUseCase deleteNoteById ${e.message}")

                -1
            }
        }
    }

    suspend fun deleteNotesByIds(ids: List<Long>): Int {
        return withContext(Dispatchers.IO) {
            try {
                noteRepository.deleteNotesByIds(ids)
            } catch (e: Exception) {
                Log.d(Constants.ERROR, "DeleteUseCase deleteNotesByIds ${e.message}")

                -1
            }
        }
    }


    suspend fun deleteImage(fileName: String): Boolean {
        return withContext(Dispatchers.Default) {
            try {
                imageRepository.deleteImage(fileName)
            } catch (e: Exception) {
                Log.d(Constants.ERROR, "DeleteUseCase deleteImage ${e.message}")
                false
            }
        }

    }
}
