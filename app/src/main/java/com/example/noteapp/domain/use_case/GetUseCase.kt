package com.example.noteapp.domain.use_case

import android.util.Log
import com.example.noteapp.common.Constants
import com.example.noteapp.data.toNote
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.INoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton

class GetUseCase @Inject constructor(
    private var noteRepository: INoteRepository,
) {

    suspend fun getNoteById(id: Long): Note? {
        return withContext(Dispatchers.IO) {
            try {
                val note = noteRepository.getNoteByID(id)
                val noteConvert = note?.toNote()
                noteConvert
            } catch (e: Exception) {
                Log.d(Constants.ERROR, "GetUseCase getNoteById ${e.message}")
                null
            }
        }

    }

    fun getAllNote(): Flow<List<Note>> {
        return try {
            noteRepository.getAllNote()
                .map { entityList ->
                    entityList.map { it.toNote() }
                }
        } catch (e: Exception) {
            Log.d(Constants.ERROR, "GetUseCase getAllNote ${e.message}")

            emptyFlow<List<Note>>()
        }

    }


}