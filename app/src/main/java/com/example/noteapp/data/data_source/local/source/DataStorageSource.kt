package com.example.noteapp.data.data_source.local.source

import android.util.Log
import com.example.noteapp.common.Constants
import com.example.noteapp.data.data_source.local.repository.IDataStorageSourceRepository
import com.example.noteapp.data.data_source.local.utils.DataStorageUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStorageSource @Inject constructor(
    private val dataStorageUtils: DataStorageUtils
) : IDataStorageSourceRepository {

   override suspend fun saveTheme(isDarkTheme: Boolean) {
        try {
            dataStorageUtils.saveTheme(isDarkTheme)


        } catch (e: Exception) {
            Log.d(Constants.ERROR, "DataStorageSource saveTheme ${e.message}")

        }
    }

    override suspend fun getTheme(): Flow<Boolean> {
        return try {
            dataStorageUtils.themeFlow
        } catch (e: Exception) {
            Log.d(Constants.ERROR, "DataStorageSource getTheme ${e.message}")
            flowOf(false) // Trả về một Flow mặc định nếu có lỗi
        }
    }


}