package com.example.noteapp.data.data_source.local.source

import androidx.paging.PagingSource
import com.example.noteapp.data.data_source.local.database.NoteDao
import com.example.noteapp.data.data_source.local.database.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteLocalDataSource @Inject constructor(
    private val noteDao: NoteDao
) {

    suspend fun insertNote(note: NoteEntity): Long {
        return noteDao.insertNote(note)
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

    fun getAllList(): Flow<List<NoteEntity>> {
        return noteDao.getAllList()
    }

    suspend fun searchNotesByTitle(title: String): Flow<List<NoteEntity>> {
        return noteDao.searchNotesByTitle(title)
    }

    suspend fun searchNotesByDate(date: String): Flow<List<NoteEntity>> {
        return noteDao.searchNotesByDate(date)

    }

    fun getPagedNotes(): PagingSource<Int, NoteEntity> {
        return noteDao.getPagedNotes()
    }

}