package com.example.noteapp.domain.use_case

import android.util.Log
import com.example.noteapp.common.Constants
import com.example.noteapp.domain.repository.IImageRepository
import com.example.noteapp.domain.repository.INoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private var noteRepository: INoteRepository,
) {
    suspend fun deleteNoteById(id: Long): Int{
        return withContext(Dispatchers.IO){
            try{
                noteRepository.deleteNoteById(id)

            }catch (e: Exception){
                Log.d(Constants.ERROR_TAG_DETAIL_SCREEN, "ERROR DELETE NOTE ${e.message}")

                -1
            }
        }
    }

}