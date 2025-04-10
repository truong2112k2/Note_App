package com.example.noteapp.common

import com.example.noteapp.data.entity.NoteEntity
import com.example.noteapp.domain.model.Note

fun NoteEntity.toNote(): Note {
    return Note(
        id = this.id,
        title = this.title,
        content = this.content,
        time = this.time,
        category = this.category,
        priority = this.priority,
        image = this.image,
        timeNotify = this.timeNotify,
        date = this.date // ✅ thêm dòng này
    )
}

fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(
        id = this.id,
        title = this.title,
        content = this.content,
        time = this.time,
        category = this.category,
        priority = this.priority,
        image = this.image,
        timeNotify = this.timeNotify,
        date = this.date // ✅ thêm dòng này
    )
}
