package com.example.noteapp.di

import android.content.Context
import androidx.room.Room
import com.example.noteapp.data.data_source.local.database.NoteDao
import com.example.noteapp.data.data_source.local.database.NoteDatabase
import com.example.noteapp.data.data_source.local.source.ImageFileDataSource
import com.example.noteapp.data.repository.ImageRepositoryImpl
import com.example.noteapp.data.repository.NoteRepositoryImpl
import com.example.noteapp.data.data_source.local.source.NoteLocalDataSource
import com.example.noteapp.data.repository.WorkManagerImpl
import com.example.noteapp.domain.repository.IWorkManager
import com.example.noteapp.domain.use_case.AddNoteUseCase
import com.example.noteapp.domain.use_case.GetNoteUseCase
import com.example.noteapp.domain.use_case.UpdateNoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent :: class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideNoteDatabase(
        @ApplicationContext context: Context
    ): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "note_database"
        ).build()
    }


    @Provides
    @Singleton
    fun provideNoteDao(noteDb: NoteDatabase): NoteDao {
        return noteDb.noteDao()
    }
}
