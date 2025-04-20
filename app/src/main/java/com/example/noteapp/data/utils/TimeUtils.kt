package com.example.noteapp.data.utils
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
        fun isNotifyTimeInPast(dateStr: String, timeStr: String): Boolean {
            val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            return try {
                val notifyDateTime = format.parse("$dateStr $timeStr")
                val now = Date()
                notifyDateTime != null && notifyDateTime.before(now)
            } catch (e: Exception) {
                false
            }
        }


}