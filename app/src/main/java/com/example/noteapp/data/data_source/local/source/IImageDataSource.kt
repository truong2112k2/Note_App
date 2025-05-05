package com.example.noteapp.data.data_source.local.source

import android.net.Uri
import java.io.File

interface IImageDataSource {

    /*
    suspend fun saveTheme(isDarkTheme: Boolean)
    suspend fun getTheme(): Flow<Boolean>
     */
    suspend fun saveImage(uri: Uri, fileName: String): String
    suspend fun getImage(fileName: String): File?
    suspend fun deleteImage(fileName: String): Boolean
}