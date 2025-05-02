package com.example.noteapp.work_manager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.noteapp.R
import kotlin.random.Random

class NotificationWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        val title = inputData.getString("note_title") ?: "New Note"
        val time = inputData.getString("note_time") ?: "00:00"

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager



        // tao channel


        val channelId = "note_channel"
        if( Build.VERSION.SDK_INT  >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                channelId,
                "Note Reminder",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(time)
            .setContentText(title)
            .setSmallIcon(R.drawable.ic_language)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(Random.nextInt(), notification)

        return Result.success()


    }
}