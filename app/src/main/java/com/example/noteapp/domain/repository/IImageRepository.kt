package com.example.noteapp.domain.repository

import android.net.Uri
import java.io.File

interface IImageRepository {
    suspend fun saveImage(uri: Uri, fileName: String): String
    suspend fun getImage(fileName: String): File?
    suspend fun deleteImage(fileName: String): Boolean
}