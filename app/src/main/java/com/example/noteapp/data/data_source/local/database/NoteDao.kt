package com.example.noteapp.data.data_source.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Update
    suspend fun updateNote(note: NoteEntity): Int

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNoteById(id: Long): Int

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1 ")
    suspend fun getNoteByID(id: Long): NoteEntity


    @Query("SELECT * FROM notes")
    fun getAllList(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' ORDER BY dateAdd DESC")
    fun searchNotesByTitle(query: String): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE dateAdd LIKE :date ORDER BY dateAdd DESC")
    fun searchNotesByDate(date: String): Flow<List<NoteEntity>>


    @Query("DELETE FROM notes WHERE id IN (:ids)")
    suspend fun deleteNotesByIds(ids: List<Long>): Int


}