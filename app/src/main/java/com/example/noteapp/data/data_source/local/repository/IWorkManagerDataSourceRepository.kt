package com.example.noteapp.data.data_source.local.repository

import android.content.Context
import com.example.noteapp.data.data_source.local.database.NoteEntity

interface IWorkManagerDataSourceRepository {


    fun scheduleNotification(context: Context, note: NoteEntity, noteId: String)

    fun cancelNoteNotification(context: Context, noteId: String)
}