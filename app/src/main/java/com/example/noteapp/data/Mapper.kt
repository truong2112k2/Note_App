package com.example.noteapp.data

import com.example.noteapp.data.data_source.local.database.NoteEntity
import com.example.noteapp.domain.model.Note

fun NoteEntity.toNote(): Note {

    return Note(
        id = this.id,
        title = this.title,
        content = this.content,
        dateAdd = this.dateAdd,
        category = this.category,
        priority = this.priority,
        image = this.image,
        timeNotify = this.timeNotify,
        dateNotify = this.dateNotify
    )
}

fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(
        id = this.id,
        title = this.title,
        content = this.content,
        dateAdd = this.dateAdd,
        category = this.category,
        priority = this.priority,
        image = this.image,
        timeNotify = this.timeNotify,
        dateNotify = this.dateNotify
    )

}
