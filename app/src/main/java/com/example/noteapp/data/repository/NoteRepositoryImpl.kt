package com.example.noteapp.data.repository

import android.util.Log
import com.example.noteapp.common.Constants
import com.example.noteapp.data.dao.NoteDao
import com.example.noteapp.data.entity.NoteEntity
import com.example.noteapp.domain.repository.INoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
): INoteRepository  {
    override suspend fun insertNote(note: NoteEntity): Long {
        Log.d(Constants.STATUS_TAG,"Insert in NoteRepositoryImpl")
        return withContext(Dispatchers.IO){
            try {
                noteDao.insertNote(note)

            }catch (e: Exception){
                Log.d(Constants.ERROR_TAG,"ERROR INSERT NOTE ${e.message}")
                -1
            }
        }
    }

    override suspend fun getAllNotes(): List<NoteEntity> {
        return withContext(Dispatchers.IO){
            try {
                noteDao.getAllNotes()

            }catch (e: Exception){
                Log.d(Constants.ERROR_TAG,"ERROR GET ALL NOTE ${e.message}")
                emptyList<NoteEntity>()
            }
        }
    }

    override suspend fun updateNote(note: NoteEntity): Int  {
        return withContext(Dispatchers.IO){
            try {
                 noteDao.updateNote(note)


            }catch (e: Exception){
                Log.d(Constants.ERROR_TAG,"ERROR UPDATE NOTE ${e.message}")
                0
            }
        }
    }

    override suspend fun deleteNoteById(id: Long): Int {
        return withContext(Dispatchers.IO){
            try {
                noteDao.deleteNoteById(id)


            }catch (e: Exception){
                Log.d(Constants.ERROR_TAG,"ERROR DELETE NOTE ${e.message}")
                0
            }
        }
    }

    override suspend fun getNoteByID(id: Long): NoteEntity? {
        return withContext(Dispatchers.IO){
            try{
                noteDao.getNoteByID(id)
            }catch (e: Exception){
                Log.d(Constants.ERROR_TAG,"ERROR DELETE NOTE ${e.message}")
                null
            }
        }
    }
}