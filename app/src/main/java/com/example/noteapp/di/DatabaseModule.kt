package com.example.noteapp.di

import android.content.Context
import androidx.room.Room
import com.example.noteapp.data.dao.NoteDao
import com.example.noteapp.data.database.NoteDatabase
import com.example.noteapp.data.repository.ImageRepositoryImpl
import com.example.noteapp.data.repository.NoteRepositoryImpl
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
    ): NoteDatabase
    {
        return Room.databaseBuilder(
            context,
             NoteDatabase :: class.java,
            "note_database"
        ).build()
    }


    @Provides
    @Singleton
    fun provideNoteDao(noteDb: NoteDatabase): NoteDao{
        return noteDb.noteDao()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(noteDao: NoteDao): NoteRepositoryImpl{
        return NoteRepositoryImpl(noteDao)
    }


   @Provides
   @Singleton
   fun provideImageRepository(@ApplicationContext context: Context): ImageRepositoryImpl{
       return ImageRepositoryImpl(context)
   }

    @Provides
    @Singleton
    fun provideAddNoteUseCase(noteRepositoryImpl: NoteRepositoryImpl, imageRepositoryImpl: ImageRepositoryImpl): AddNoteUseCase{
        return AddNoteUseCase(noteRepositoryImpl, imageRepositoryImpl)
    }

    @Provides
    @Singleton
    fun provideGetNoteUseCase(noteRepositoryImpl: NoteRepositoryImpl) : GetNoteUseCase{
        return  GetNoteUseCase(noteRepositoryImpl)
    }


    @Provides
    @Singleton
    fun provideUpdateNoteUseCase(noteRepositoryImpl: NoteRepositoryImpl, imageRepositoryImpl: ImageRepositoryImpl) : UpdateNoteUseCase {
        return UpdateNoteUseCase(noteRepositoryImpl, imageRepositoryImpl)

    }
}