package com.example.noteapp.data.data_source.local.source

import android.util.Log
import com.example.noteapp.common.Constants
import com.example.noteapp.data.data_source.local.database.NoteDao
import com.example.noteapp.data.data_source.local.database.NoteEntity
import com.example.noteapp.data.data_source.local.repository.INoteDataSourceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NoteDataSource @Inject constructor(
    private val noteDao: NoteDao
): INoteDataSourceRepository {
     override suspend fun insertNote(note: NoteEntity): Long {
        Log.d(Constants.STATUS, "NoteLocalDataSource InsertNote")
        return withContext(Dispatchers.IO) {
            try {
                noteDao.insertNote(note)

            } catch (e: Exception) {
                Log.d(Constants.ERROR, "NoteLocalDataSource InsertNote ${e.message}")
                -1
            }
        }
    }


    override  suspend fun updateNote(note: NoteEntity): Int {
        return withContext(Dispatchers.IO) {
            try {
                noteDao.updateNote(note)


            } catch (e: Exception) {
                Log.d(Constants.ERROR, "NoteLocalDataSource UpdateNote ${e.message}")
                0
            }
        }
    }

    override   suspend fun deleteNoteById(id: Long): Int {
        return withContext(Dispatchers.IO) {
            try {
                noteDao.deleteNoteById(id)


            } catch (e: Exception) {
                Log.d(Constants.ERROR, "NoteLocalDataSource deleteNoteById ${e.message}")
                0
            }
        }
    }

    override   suspend fun getNoteByID(id: Long): NoteEntity? {
        return withContext(Dispatchers.IO) {
            try {
                noteDao.getNoteByID(id)
            } catch (e: Exception) {
                Log.d(Constants.ERROR, "NoteLocalDataSource getNoteByID ${e.message}")
                null
            }
        }
    }

    override     fun getAllNote(): Flow<List<NoteEntity>> {
        try {
            return noteDao.getAllList()
        } catch (e: Exception) {
            Log.d(Constants.ERROR, "NoteLocalDataSource getAllNote ${e.message}")
            return emptyFlow<List<NoteEntity>>()
        }

    }

    override  suspend fun searchNotesByTitle(title: String): Flow<List<NoteEntity>> {
         return  try {
             noteDao.searchNotesByTitle(title)
        } catch (e: Exception) {
             Log.d(Constants.ERROR, "NoteLocalDataSource searchNotesByTitle ${e.message}")
             emptyFlow<List<NoteEntity>>()
        }
    }

    override  suspend fun searchNotesByDate(date: String): Flow<List<NoteEntity>> {
         return try {
             noteDao.searchNotesByDate(date)
        } catch (e: Exception) {
             Log.d(Constants.ERROR, "NoteLocalDataSource searchNotesByDate ${e.message}")
             emptyFlow<List<NoteEntity>>()
        }
    }


    override  suspend fun deleteNotesByIds(ids: List<Long>): Int{
        return withContext(Dispatchers.IO){
            try{
                  noteDao.deleteNotesByIds(ids)
            }catch (e: Exception){
                Log.d(Constants.ERROR, "NoteLocalDataSource deleteNotesByIds ${e.message}")

                -1
            }
        }
    }




}