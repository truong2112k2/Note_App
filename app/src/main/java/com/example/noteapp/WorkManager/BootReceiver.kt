package com.example.noteapp.WorkManager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.noteapp.data.preferences.NotePreferences
import java.util.Calendar
import java.util.concurrent.TimeUnit

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            // Lấy thời gian đã lưu từ SharedPreferences
            val savedTime = NotePreferences. getSavedTimeFromPreferences(context)

            // Nếu có thời gian đã lưu
            savedTime?.let { (hour, minute) ->
                val now = Calendar.getInstance()
                val targetTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                }

                val delay = targetTime.timeInMillis - now.timeInMillis
                if (delay > 0) { // Chỉ đặt thông báo khi thời gian chưa đến
                    val workManager = WorkManager.getInstance(context)
                    val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                        .build()
                    workManager.enqueue(workRequest)
                }
            }
        }
    }
}

//class BootReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent?) {
//        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
//            // Lấy WorkManager và khởi động lại công việc
//            val workManager = WorkManager.getInstance(context)
//
//            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
//                .setInitialDelay(10, TimeUnit.SECONDS) // Hoặc đặt lại thời gian đã lưu trước đó
//                .build()
//
//            workManager.enqueue(workRequest)
//        }
//    }
//}
