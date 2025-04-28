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
                Log.d(Constants.ERROR_TAG_DETAIL_SCREEN, "ERROR DELETE NOTE ${e.message}")

                -1
            }
        }
    }

    suspend fun deleteNotesByIds(ids: List<Long>): Int {
        return withContext(Dispatchers.IO) {
            try {
                noteRepository.deleteNotesByIds(ids)
            } catch (e: Exception) {
                -1
            }
        }
    }


    suspend fun deleteImage(fileName: String): Boolean {
        return withContext(Dispatchers.Default) {
            try {
                imageRepository.deleteImage(fileName)
            } catch (e: Exception) {
                Log.d(Constants.ERROR_TAG_DETAIL_SCREEN, "ERROR DELETE IMAGE ${e.message}")
                false
            }
        }

    }
}
