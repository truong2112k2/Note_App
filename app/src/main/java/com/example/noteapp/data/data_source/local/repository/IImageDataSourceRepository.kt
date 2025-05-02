package com.example.noteapp.data.data_source.local.repository

import android.net.Uri
import java.io.File

interface IImageDataSourceRepository {

    /*
    suspend fun saveTheme(isDarkTheme: Boolean)
    suspend fun getTheme(): Flow<Boolean>
     */
    suspend fun saveImage(uri: Uri, fileName: String): String
    suspend fun getImage(fileName: String): File?
    suspend fun deleteImage(fileName: String): Boolean
}