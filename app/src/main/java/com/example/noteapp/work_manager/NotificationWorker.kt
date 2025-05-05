package com.example.noteapp.work_manager

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.noteapp.MainActivity
import com.example.noteapp.R

import kotlin.random.Random

class NotificationWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {
    @SuppressLint("Wakelock")
    override fun doWork(): Result {
        val title = inputData.getString("note_title") ?: "New Note"
        val time = inputData.getString("note_time") ?: "00:00"

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager



        /// am thanh
        val soundUri = Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${applicationContext.packageName}/raw/notify")
        val attributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        // xet click
        val intent = Intent(applicationContext, MainActivity :: class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        val channelId = "note_channel"
        if( Build.VERSION.SDK_INT  >= Build.VERSION_CODES.O){

            val channel = NotificationChannel(
                channelId,
                "Note Reminder",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setSound(soundUri, attributes )
            }
            notificationManager.createNotificationChannel(channel)
        }


        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(time)
            .setSmallIcon(R.drawable.ic_notify)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(Random.nextInt(), notification)

        return Result.success()


    }
}