package com.example.noteapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.noteapp.data.dao.NoteDao
import com.example.noteapp.data.entity.NoteEntity
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized


@Database(entities = [NoteEntity :: class], version = 1, exportSchema = false)
abstract  class NoteDatabase: RoomDatabase() {

    abstract fun noteDao() : NoteDao

}