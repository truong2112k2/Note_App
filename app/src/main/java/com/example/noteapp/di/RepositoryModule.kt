package com.example.noteapp.di

import com.example.noteapp.data.data_source.local.source.IDataStorageDataSource
import com.example.noteapp.data.data_source.local.source.IImageDataSource
import com.example.noteapp.data.data_source.local.source.INoteDataSource
import com.example.noteapp.data.data_source.local.source.IWorkManagerDataSource
import com.example.noteapp.data.data_source.local.source.impl.DataStorageSource
import com.example.noteapp.data.data_source.local.source.impl.ImageDataSource
import com.example.noteapp.data.data_source.local.source.impl.NoteDataSource
import com.example.noteapp.data.data_source.local.source.impl.WorkManagerDataSource
import com.example.noteapp.data.repository.DataStorageImpl
import com.example.noteapp.data.repository.ImageRepositoryImpl
import com.example.noteapp.data.repository.NoteRepositoryImpl
import com.example.noteapp.data.repository.WorkManagerImpl
import com.example.noteapp.domain.repository.IDataStorageRepository
import com.example.noteapp.domain.repository.IImageRepository
import com.example.noteapp.domain.repository.INoteRepository
import com.example.noteapp.domain.repository.IWorkManagerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindNoteRepository(noteRepositoryImpl: NoteRepositoryImpl): INoteRepository

    @Binds
    @Singleton
    abstract fun bindImageRepository(imageRepositoryImpl: ImageRepositoryImpl): IImageRepository


    @Binds
    @Singleton
    abstract fun bindWorkManagerRepository(workManagerImpl: WorkManagerImpl): IWorkManagerRepository

    @Binds
    @Singleton
    abstract fun bindDataStorageRepository(dataStorageImpl: DataStorageImpl): IDataStorageRepository

    @Binds
    @Singleton
    abstract fun bindIDataStorageRepository(dataStorageSource: DataStorageSource): IDataStorageDataSource

    @Binds
    @Singleton
    abstract fun bindIImageDataSourceRepository(imageDataSource: ImageDataSource): IImageDataSource

    @Binds
    @Singleton
    abstract fun bindINoteDataSourceRepository(noteDataSource: NoteDataSource): INoteDataSource

    @Binds
    @Singleton
    abstract fun bindIWorkManagerDataSourceRepository(workManagerDataSource: WorkManagerDataSource): IWorkManagerDataSource
}

