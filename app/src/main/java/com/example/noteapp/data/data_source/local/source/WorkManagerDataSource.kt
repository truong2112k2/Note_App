package com.example.noteapp.data.data_source.local.source

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.noteapp.WorkManager.NotificationWorker
import com.example.noteapp.common.Constants
import com.example.noteapp.data.data_source.local.database.NoteEntity
import com.example.noteapp.domain.model.Note
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkManagerDataSource @Inject constructor() {

    @RequiresApi(Build.VERSION_CODES.O)
     fun scheduleNotification(context: Context, note: NoteEntity) {
        Log.d(Constants.STATUS_TAG_ADD_NOTE_SCREEN, "scheduleNotification")
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.getDefault())
        val dateTimeString = "${note.dateNotify} ${note.timeNotify}" // ví dụ: "12/04/2025 14:30"
        val localDateTime = LocalDateTime.parse(dateTimeString, formatter)
        val delayMillis = Duration.between(LocalDateTime.now(), localDateTime).toMillis()

        if (delayMillis <= 0) return // thời gian đã qua

        val data = Data.Builder()
            .putString("note_title", note.title)
            .putString("note_time", note.timeNotify)
            .build()
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()
        WorkManager.getInstance(context ).enqueue(workRequest)
    }


}