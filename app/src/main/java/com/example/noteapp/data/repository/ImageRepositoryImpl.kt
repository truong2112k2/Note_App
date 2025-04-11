package com.example.noteapp.data.repository

import android.content.Context
import android.net.Uri
import com.example.noteapp.data.utils.ImageUtils
import com.example.noteapp.domain.repository.IImageRepository
import java.io.File

class ImageRepositoryImpl(private val context: Context) : IImageRepository {
    override suspend fun saveImage(uri: Uri, fileName: String): String {
        return ImageUtils.saveImageToInternalStorage(context = context, uri, fileName)
    }

    override suspend fun getImage(fileName: String): File? {
        return ImageUtils.readImageFile(context, fileName)
    }

    override suspend fun deleteImage(fileName: String): Boolean {
        return ImageUtils.deleteImageFile(context, fileName)
    }
}