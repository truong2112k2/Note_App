package com.example.noteapp.domain.model

import android.graphics.Color
import androidx.core.graphics.ColorUtils
import com.example.noteapp.common.ColorUtils.getRandomLightColorExcludingBlue
import kotlin.random.Random
import kotlin.random.nextInt

data class Note (
        val id: Long = 0,
        val title: String,
        val content: String,
        val dateAdd: String, // Unix timestamp (time in milliseconds)
        val category: String,
        val priority: Int, // Priority can be 1 (high), 2 (medium), 3 (low), etc.
        val image: String?, // Image URL or path to the image
        val timeNotify: String,// Unix timestamp for notification time
        val dateNotify: String, //  ngày tạo (ví dụ: "2025-04-08")
        val height: Int = Random.nextInt(20..100), // Chiều cao ngẫu nhiên từ 100 đến 400
        val isSelected : Boolean = false


){



}

