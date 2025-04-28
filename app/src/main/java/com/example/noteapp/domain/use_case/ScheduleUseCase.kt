package com.example.noteapp.domain.use_case

import android.content.Context
import android.util.Log
import com.example.noteapp.data.toNoteEntity
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.IWorkManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton

class ScheduleUseCase @Inject constructor(
    private val workManager: IWorkManager
) {
    suspend fun scheduleNotification(context: Context, note: Note, noteId: String) {

        try{
            Log.d("2312321","note ban dau in usecase ${note.id}")
            workManager.scheduleNotification(context, note.toNoteEntity(), noteId)
        }catch (e: Exception){
            Log.d("2312321","97878877")

        }
    }
    suspend fun cancelScheduleNotification(context: Context, noteId: String){
        try {
            workManager.cancelNoteNotification(context, noteId)

        }catch (e: Exception){
            Log.d("2312321","97878877")

        }
    }
}