package com.example.noteapp.domain.repository

import android.content.Context
import com.example.noteapp.data.data_source.local.database.NoteEntity

interface IWorkManagerRepository {
    suspend fun scheduleNotification(context: Context, note: NoteEntity, noteId: String)
    suspend fun cancelNoteNotification(context: Context, noteId: String,)
}