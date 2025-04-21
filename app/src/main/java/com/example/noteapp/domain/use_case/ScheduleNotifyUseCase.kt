package com.example.noteapp.domain.use_case

import android.content.Context
import com.example.noteapp.common.toNoteEntity
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.IWorkManager
import javax.inject.Inject

class ScheduleNotifyUseCase @Inject constructor(
    private val workManager: IWorkManager
) {
    suspend fun scheduleNotification(context: Context, note: Note) {
        workManager.scheduleNotification(context, note.toNoteEntity())
    }

}