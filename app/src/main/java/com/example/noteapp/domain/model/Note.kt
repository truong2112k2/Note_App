package com.example.noteapp.domain.model

class Note (
        val id: Long = 0,
        val title: String,
        val content: String,
        val time: String, // Unix timestamp (time in milliseconds)
        val category: String,
        val priority: Int, // Priority can be 1 (high), 2 (medium), 3 (low), etc.
        val image: String?, // Image URL or path to the image
        val timeNotify: String ,// Unix timestamp for notification time
        val date: String //  ngày tạo (ví dụ: "2025-04-08")

){

}