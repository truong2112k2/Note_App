package com.example.noteapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    val dateAdd: String, // time created
    val category: String,
    val priority: Int,
    val image: String?,
    val timeNotify: String,
    val dateNotify: String // ngày tạo (ví dụ: "2025-04-08")
)
