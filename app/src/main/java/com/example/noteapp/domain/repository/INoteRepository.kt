package com.example.noteapp.domain.repository

import com.example.noteapp.data.entity.NoteEntity

interface INoteRepository {

    suspend  fun insertNote(note: NoteEntity): Long

    suspend fun getAllNotes(): List<NoteEntity>

    suspend fun updateNote(note: NoteEntity): Int

    suspend fun deleteNoteById(id: Long): Int

    suspend fun getNoteByID(id: Long ): NoteEntity?

}