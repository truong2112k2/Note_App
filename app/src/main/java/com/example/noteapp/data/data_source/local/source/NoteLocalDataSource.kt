package com.example.noteapp.data.data_source.local.source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.noteapp.common.Constants
import com.example.noteapp.data.data_source.local.database.NoteDao
import com.example.noteapp.data.data_source.local.database.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NoteLocalDataSource @Inject constructor(
    private val noteDao: NoteDao
) {
     suspend fun insertNote(note: NoteEntity): Long {
        Log.d(Constants.STATUS_TAG_ADD_NOTE_SCREEN, "Insert in NoteRepositoryImpl")
        return withContext(Dispatchers.IO) {
            try {
                noteDao.insertNote(note)

            } catch (e: Exception) {
                Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, "ERROR INSERT NOTE ${e.message}")
                -1
            }
        }
    }


     suspend fun updateNote(note: NoteEntity): Int {
        return withContext(Dispatchers.IO) {
            try {
                noteDao.updateNote(note)


            } catch (e: Exception) {
                Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, "ERROR UPDATE NOTE ${e.message}")
                0
            }
        }
    }

     suspend fun deleteNoteById(id: Long): Int {
        return withContext(Dispatchers.IO) {
            try {
                noteDao.deleteNoteById(id)


            } catch (e: Exception) {
                Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, "ERROR DELETE NOTE ${e.message}")
                0
            }
        }
    }

     suspend fun getNoteByID(id: Long): NoteEntity? {
        return withContext(Dispatchers.IO) {
            try {
                noteDao.getNoteByID(id)
            } catch (e: Exception) {
                Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, "ERROR DELETE NOTE ${e.message}")
                null
            }
        }
    }

     fun getAllNote(): Flow<List<NoteEntity>> {
        try {
            return noteDao.getAllList()
        } catch (e: Exception) {
            Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, "ERROR DELETE NOTE ${e.message}")
            return emptyFlow<List<NoteEntity>>()
        }

    }

     suspend fun searchNotesByTitle(title: String): Flow<List<NoteEntity>> {
         return  try {
             noteDao.searchNotesByTitle(title)
        } catch (e: Exception) {
            Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, "ERROR DELETE NOTE ${e.message}")
             emptyFlow<List<NoteEntity>>()
        }
    }

     suspend fun searchNotesByDate(date: String): Flow<List<NoteEntity>> {
         return try {
             noteDao.searchNotesByDate(date)
        } catch (e: Exception) {
            Log.d(Constants.ERROR_TAG_ADD_NOTE_SCREEN, "ERROR DELETE NOTE ${e.message}")
             emptyFlow<List<NoteEntity>>()
        }
    }

    fun getPagedNotes(): PagingSource<Int, NoteEntity> {
        return try {
            noteDao.getPagedNotes()
        } catch (e: Exception) {
            // Ghi log lỗi hoặc trả về một PagingSource trống
            Log.e("Error", "Failed to fetch paged notes", e)
            // Hoặc trả về PagingSource trống
            object : PagingSource<Int, NoteEntity>() {
                override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NoteEntity> {
                    return LoadResult.Error(e)  // Trả về lỗi
                }

                override fun getRefreshKey(state: PagingState<Int, NoteEntity>): Int? {
                    TODO("Not yet implemented")
                }
            }
        }
    }

    suspend fun deleteNotesByIds(ids: List<Long>): Int{
        return withContext(Dispatchers.IO){
            try{
                  noteDao.deleteNotesByIds(ids)
            }catch (e: Exception){
                -1
            }
        }
    }



}