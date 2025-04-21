package com.example.noteapp.data.data_source.local.source

import com.example.noteapp.data.data_source.local.database.NoteDao
import com.example.noteapp.data.data_source.local.database.NoteEntity
import javax.inject.Inject

class NoteLocalDataSource @Inject constructor(
    private val noteDao: NoteDao
) {

    suspend fun insertNote(note: NoteEntity): Long {
        return noteDao.insertNote(note)
    }

    suspend fun getAllNotes(): List<NoteEntity> {
        return noteDao.getAllNotes()
    }

    suspend fun updateNote(note: NoteEntity): Int {
        return noteDao.updateNote(note)


    }

    suspend fun deleteNoteById(id: Long): Int {
        return noteDao.deleteNoteById(id)


    }

    suspend fun getNoteByID(id: Long): NoteEntity? {
        return noteDao.getNoteByID(id)
    }
}