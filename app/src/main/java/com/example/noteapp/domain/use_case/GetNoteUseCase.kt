package com.example.noteapp.domain.use_case

import android.util.Log
import com.example.noteapp.common.Constants
import com.example.noteapp.common.toNote
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.INoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private var noteRepository: INoteRepository,
) {

    suspend fun getAllNote(): List<Note> {
        return withContext(Dispatchers.IO) {
            try {
                val listNoteEntity = noteRepository.getAllNotes()
                listNoteEntity.map { it.toNote() }
            } catch (e: Exception) {
                Log.d(Constants.ERROR_TAG, e.message.toString())
                emptyList<Note>()
            }
        }

    }
    suspend fun getNoteById(id: Long ): Note? {
        return withContext(Dispatchers.IO) {
            try {
                val listNoteEntity = noteRepository.getNoteByID(id)
                val note =  listNoteEntity?.toNote()
                note
            } catch (e: Exception) {
                Log.d(Constants.ERROR_TAG, e.message.toString())
                null
            }
        }

    }


}