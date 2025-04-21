package com.example.noteapp.data.data_source.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Query("SELECT * FROM notes ORDER BY dateAdd DESC")
    suspend fun getAllNotes(): List<NoteEntity>

    @Update
    suspend fun updateNote(note: NoteEntity): Int

    @Query("DELETE FROM notes WHERE id = :id")
    suspend  fun deleteNoteById(id: Long): Int

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1 ")
    suspend fun getNoteByID(id: Long ): NoteEntity
}