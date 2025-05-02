package com.example.noteapp.domain.use_case

import android.util.Log
import com.example.noteapp.common.Constants
import com.example.noteapp.domain.repository.IDataStorageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DataStorageUseCase @Inject constructor(
    private val dataStorage: IDataStorageRepository
) {
    suspend fun saveTheme(isDarkTheme: Boolean) {

        try {
            dataStorage.saveTheme(isDarkTheme)

        } catch (e: Exception) {
            Log.d(Constants.ERROR, "DataStorageUseCase saveTheme ${e.message}")
        }
    }

    suspend fun getTheme(): Flow<Boolean> {
        return try {
            dataStorage.getTheme()

        } catch (e: Exception) {
            Log.d(Constants.ERROR, "DataStorageUseCase getTheme ${e.message}")
            flowOf(false) // Trả về một Flow mặc định nếu có lỗi

        }
    }

}