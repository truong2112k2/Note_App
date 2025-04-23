package com.example.noteapp.domain.use_case

import android.util.Log
import androidx.paging.PagingSource
import com.example.noteapp.common.Constants
import com.example.noteapp.data.data_source.local.database.NoteEntity
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

class GetNoteUseCase @Inject constructor(
    private var noteRepository: INoteRepository,
) {

    suspend fun getNoteById(id: Long ): Note? {
        return withContext(Dispatchers.IO) {
            try {
                val note = noteRepository.getNoteByID(id)
                val noteConvert =  note?.toNote()
                noteConvert
            } catch (e: Exception) {
                Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, e.message.toString())
                null
            }
        }

    }

    fun getAllNote(): Flow<List<Note>> {
        return   try{
             noteRepository.getAllNote()
                .map { entityList ->
                    entityList.map { it.toNote() }
                }
        }catch (e: Exception){
            emptyFlow<List<Note>>()
        }

    }
    fun getPagedNotes(): PagingSource<Int, NoteEntity> {
         return noteRepository.getPagedNotes()
     }


}