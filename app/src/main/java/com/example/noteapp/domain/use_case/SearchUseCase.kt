package com.example.noteapp.domain.use_case

import android.annotation.SuppressLint
import android.util.Log
import com.example.noteapp.common.Constants
import com.example.noteapp.data.toNote
import com.example.noteapp.domain.model.Note
import com.example.noteapp.domain.repository.INoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SearchUseCase @Inject constructor(
    private val noteRepository: INoteRepository
) {
    @SuppressLint("SuspiciousIndentation")
    suspend fun searchNotesByTitle(title: String): Flow<List<Note>> {
        return withContext(Dispatchers.IO) {
            try {
                val listNoteEntity = noteRepository.searchNotesByTitle(title)

                listNoteEntity.map {
                    it.map {
                        it.toNote()
                    }
                }


            } catch (e: Exception) {
                Log.d(Constants.ERROR, "SearchUseCase searchNotesByTitle ${e.message}")

                emptyFlow<List<Note>>()
            }
        }
    }

    suspend fun searchNotesByDate(date: String): Flow<List<Note>> {
        return withContext(Dispatchers.IO) {
            try {
                val listNoteEntity = noteRepository.searchNotesByDate(date)
                listNoteEntity.map {
                    it.map {
                        it.toNote()
                    }
                }
            } catch (e: Exception) {
                Log.d(Constants.ERROR, "SearchUseCase searchNotesByDate ${e.message}")
                emptyFlow<List<Note>>()
            }

        }

    }
}