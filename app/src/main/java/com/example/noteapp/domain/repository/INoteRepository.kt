package com.example.noteapp.domain.repository

import androidx.paging.PagingSource
import com.example.noteapp.data.data_source.local.database.NoteEntity
import kotlinx.coroutines.flow.Flow

interface INoteRepository {

    suspend fun insertNote(note: NoteEntity): Long
    fun getAllNote(): Flow<List<NoteEntity>>
    suspend fun updateNote(note: NoteEntity): Int
    suspend fun deleteNoteById(id: Long): Int
    suspend fun getNoteByID(id: Long): NoteEntity?



    suspend fun searchNotesByTitle(title: String): Flow<List<NoteEntity>>

    suspend fun searchNotesByDate(date: String): Flow<List<NoteEntity>>


    fun getPagedNotes(): PagingSource<Int, NoteEntity>

}