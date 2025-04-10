package com.example.noteapp.data.preferences

import android.content.Context

object NotePreferences {

    // Lưu thời gian vào SharedPreferences với try-catch và trả về Boolean
    fun saveTimeToPreferences(context: Context, hour: Int, minute: Int): Boolean {
        return try {
            val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            sharedPreferences.edit().apply {
                putInt("selectedHour", hour)
                putInt("selectedMinute", minute)
                apply()
            }
            true // Lưu thành công
        } catch (e: Exception) {
            e.printStackTrace()
            false // Lưu thất bại
        }
    }

    // Lấy thời gian đã lưu từ SharedPreferences với try-catch
    fun getSavedTimeFromPreferences(context: Context): Pair<Int, Int>? {
        return try {
            val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            val hour = sharedPreferences.getInt("selectedHour", -1)
            val minute = sharedPreferences.getInt("selectedMinute", -1)

            if (hour != -1 && minute != -1) Pair(hour, minute) else null
        } catch (e: Exception) {
            e.printStackTrace()
            null // Lỗi xảy ra, trả về null
        }
    }

}