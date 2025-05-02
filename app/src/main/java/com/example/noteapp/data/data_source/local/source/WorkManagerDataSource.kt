package com.example.noteapp.data.data_source.local.source

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.noteapp.common.Constants
import com.example.noteapp.data.data_source.local.database.NoteEntity
import com.example.noteapp.data.data_source.local.repository.IWorkManagerDataSourceRepository
import com.example.noteapp.work_manager.NotificationWorker
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkManagerDataSource @Inject constructor() : IWorkManagerDataSourceRepository {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun scheduleNotification(context: Context, note: NoteEntity, noteId: String) {

        try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.getDefault())
            val dateTimeString = "${note.dateNotify} ${note.timeNotify}"
            val localDateTime = LocalDateTime.parse(dateTimeString, formatter)
            val delayMillis = Duration.between(LocalDateTime.now(), localDateTime).toMillis()

            if (delayMillis <= 0) return

            val data = Data.Builder()
                .putString("note_title", note.title)
                .putString("note_time", note.timeNotify)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                .setInputData(data)
                .addTag(noteId)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
            Log.d(Constants.STATUS, "WorkManagerDataSource: note id scheduleNotification ${noteId}")
        } catch (e: Exception) {
            Log.d(Constants.ERROR, "WorkManagerDataSource-scheduleNotification: ${e.message}")
        }


    }


    override fun cancelNoteNotification(context: Context, noteId: String) {

        try {
            WorkManager.getInstance(context).cancelAllWorkByTag(noteId)
        } catch (e: Exception) {
            Log.d(Constants.ERROR, "WorkManagerDataSource-cancelNoteNotification: ${e.message}")

        }


    }

}