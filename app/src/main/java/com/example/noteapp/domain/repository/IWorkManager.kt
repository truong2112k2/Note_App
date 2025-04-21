package com.example.noteapp.domain.repository

import android.content.Context
import com.example.noteapp.data.data_source.local.database.NoteEntity
import com.example.noteapp.domain.model.Note

interface IWorkManager {
    suspend fun scheduleNotification(context: Context, note: NoteEntity)
}