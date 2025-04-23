package com.example.noteapp.data.repository

import android.util.Log
import androidx.paging.PagingSource
import com.example.noteapp.common.Constants
import com.example.noteapp.data.data_source.local.database.NoteEntity
import com.example.noteapp.data.data_source.local.source.NoteLocalDataSource
import com.example.noteapp.domain.repository.INoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteLocalDataSource: NoteLocalDataSource
) : INoteRepository {
    override suspend fun insertNote(note: NoteEntity): Long {
        Log.d(Constants.STATUS_TAG_ADD_NOTE_SCREEN, "Insert in NoteRepositoryImpl")
        return withContext(Dispatchers.IO) {
            try {
                noteLocalDataSource.insertNote(note)

            } catch (e: Exception) {
                Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, "ERROR INSERT NOTE ${e.message}")
                -1
            }
        }
    }


    override suspend fun updateNote(note: NoteEntity): Int {
        return withContext(Dispatchers.IO) {
            try {
                noteLocalDataSource.updateNote(note)


            } catch (e: Exception) {
                Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, "ERROR UPDATE NOTE ${e.message}")
                0
            }
        }
    }

    override suspend fun deleteNoteById(id: Long): Int {
        return withContext(Dispatchers.IO) {
            try {
                noteLocalDataSource.deleteNoteById(id)


            } catch (e: Exception) {
                Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, "ERROR DELETE NOTE ${e.message}")
                0
            }
        }
    }

    override suspend fun getNoteByID(id: Long): NoteEntity? {
        return withContext(Dispatchers.IO) {
            try {
                noteLocalDataSource.getNoteByID(id)
            } catch (e: Exception) {
                Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, "ERROR DELETE NOTE ${e.message}")
                null
            }
        }
    }

    override fun getAllNote(): Flow<List<NoteEntity>> {
        try {
            return noteLocalDataSource.getAllList()
        } catch (e: Exception) {
            Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, "ERROR DELETE NOTE ${e.message}")
            return emptyFlow<List<NoteEntity>>()
        }

    }

    override suspend fun searchNotesByTitle(title: String): Flow<List<NoteEntity>> {
        try {
            return noteLocalDataSource.searchNotesByTitle(title)
        } catch (e: Exception) {
            Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, "ERROR DELETE NOTE ${e.message}")
            return emptyFlow<List<NoteEntity>>()
        }
    }

    override suspend fun searchNotesByDate(date: String): Flow<List<NoteEntity>> {
        try {
            return noteLocalDataSource.searchNotesByDate(date)
        } catch (e: Exception) {
            Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, "ERROR DELETE NOTE ${e.message}")
            return emptyFlow<List<NoteEntity>>()
        }
    }

    override  fun getPagedNotes(): PagingSource<Int, NoteEntity> {
       // try {
            return noteLocalDataSource.getPagedNotes()
       // } catch (e: Exception) {
         //   Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, "ERROR DELETE NOTE ${e.message}")
      // PagingSource.empty() trả về một PagingSource rỗng

      //  }
    }
}