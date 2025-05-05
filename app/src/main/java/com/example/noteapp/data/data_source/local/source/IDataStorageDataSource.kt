package com.example.noteapp.data.data_source.local.source

import kotlinx.coroutines.flow.Flow

interface IDataStorageDataSource {

    suspend fun saveTheme(isDarkTheme: Boolean)
    suspend fun getTheme(): Flow<Boolean>
}