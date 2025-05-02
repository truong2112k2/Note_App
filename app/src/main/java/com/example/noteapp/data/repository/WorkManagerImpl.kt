package com.example.noteapp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import com.example.noteapp.data.data_source.local.database.NoteEntity
import com.example.noteapp.data.data_source.local.repository.IWorkManagerDataSourceRepository
import com.example.noteapp.data.data_source.local.source.WorkManagerDataSource
import com.example.noteapp.domain.repository.IWorkManagerRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WorkManagerImpl @Inject constructor(
   private val workManagerDataSource: IWorkManagerDataSourceRepository
): IWorkManagerRepository {

    @SuppressLint("NewApi")
    override suspend fun scheduleNotification(context: Context, note: NoteEntity, noteId: String) {

        workManagerDataSource.scheduleNotification(context, note, noteId)
    }

    override suspend fun cancelNoteNotification(context: Context, noteId: String) {
        workManagerDataSource.cancelNoteNotification(context, noteId)
    }
}