package com.example.noteapp.data.data_source.local.source

import com.example.noteapp.data.data_source.local.utils.DataStorageUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStorageSource @Inject constructor(
    private val dataStorageUtils: DataStorageUtils
) {

    suspend fun saveTheme(isDarkTheme: Boolean){
        dataStorageUtils.saveTheme(isDarkTheme)
    }

    suspend fun getTheme(): Flow<Boolean>{
        return dataStorageUtils.themeFlow
    }


}