package com.example.noteapp.data.data_source.local.repository

import com.example.noteapp.data.data_source.local.database.NoteEntity
import kotlinx.coroutines.flow.Flow

interface INoteDataSourceRepository {
    suspend fun insertNote(note: NoteEntity): Long


    suspend fun updateNote(note: NoteEntity): Int

    suspend fun deleteNoteById(id: Long): Int

    suspend fun getNoteByID(id: Long): NoteEntity?

    fun getAllNote(): Flow<List<NoteEntity>>

    suspend fun searchNotesByTitle(title: String): Flow<List<NoteEntity>>
    suspend fun searchNotesByDate(date: String): Flow<List<NoteEntity>>

    suspend fun deleteNotesByIds(ids: List<Long>): Int


}