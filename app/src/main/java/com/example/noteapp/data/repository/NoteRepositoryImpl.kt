package com.example.noteapp.data.repository

import com.example.noteapp.data.data_source.local.database.NoteEntity
import com.example.noteapp.data.data_source.local.source.INoteDataSource
import com.example.noteapp.domain.repository.INoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteLocalDataSource: INoteDataSource
) : INoteRepository {

    override suspend fun insertNote(note: NoteEntity): Long {
        return withContext(Dispatchers.IO) {
            noteLocalDataSource.insertNote(note)
        }
    }


    override suspend fun updateNote(note: NoteEntity): Int {
        return withContext(Dispatchers.IO) {
            noteLocalDataSource.updateNote(note)
        }
    }

    override suspend fun deleteNoteById(id: Long): Int {
        return withContext(Dispatchers.IO) {
            noteLocalDataSource.deleteNoteById(id)
        }
    }

    override suspend fun getNoteByID(id: Long): NoteEntity? {
        return withContext(Dispatchers.IO) {
            noteLocalDataSource.getNoteByID(id)
        }
    }

    override fun getAllNote(): Flow<List<NoteEntity>> {
        return noteLocalDataSource.getAllNote()
    }

    override suspend fun searchNotesByTitle(title: String): Flow<List<NoteEntity>> {
        return noteLocalDataSource.searchNotesByTitle(title)
    }

    override suspend fun searchNotesByDate(date: String): Flow<List<NoteEntity>> {
        return noteLocalDataSource.searchNotesByDate(date)
    }


    override suspend fun deleteNotesByIds(ids: List<Long>): Int {
        return noteLocalDataSource.deleteNotesByIds(ids)
    }


}


