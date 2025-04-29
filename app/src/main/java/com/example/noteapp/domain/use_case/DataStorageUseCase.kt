package com.example.noteapp.domain.use_case

import com.example.noteapp.data.repository.DataStorageImpl
import com.example.noteapp.domain.repository.IDataStorageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DataStorageUseCase @Inject constructor(
    private val dataStorage: IDataStorageRepository
) {
    suspend fun saveTheme(isDarkTheme: Boolean){
        dataStorage.saveTheme(isDarkTheme)
    }

    suspend fun getTheme(): Flow<Boolean>{
        return dataStorage.getTheme()
    }

}