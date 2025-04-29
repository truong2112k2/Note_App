package com.example.noteapp.data.repository

import com.example.noteapp.data.data_source.local.source.DataStorageSource
import com.example.noteapp.domain.repository.IDataStorageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStorageImpl @Inject constructor( private val dataStorageSource: DataStorageSource): IDataStorageRepository {

    override suspend fun saveTheme(isDarkTheme: Boolean) {
        dataStorageSource.saveTheme(isDarkTheme)
    }

    override suspend fun getTheme(): Flow<Boolean> {
       return dataStorageSource.getTheme()
    }
}