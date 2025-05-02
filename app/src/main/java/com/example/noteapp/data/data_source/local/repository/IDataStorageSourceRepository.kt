package com.example.noteapp.data.data_source.local.repository

import kotlinx.coroutines.flow.Flow

interface IDataStorageSourceRepository {

    suspend fun saveTheme(isDarkTheme: Boolean)
    suspend fun getTheme(): Flow<Boolean>
}