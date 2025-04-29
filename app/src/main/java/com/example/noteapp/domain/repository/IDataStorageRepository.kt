package com.example.noteapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface IDataStorageRepository {
    suspend fun saveTheme(isDarkTheme: Boolean)

    suspend fun getTheme(): Flow<Boolean>
}