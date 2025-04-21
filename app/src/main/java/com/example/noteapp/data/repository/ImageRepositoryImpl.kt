package com.example.noteapp.data.repository

import android.content.Context
import android.net.Uri
import com.example.noteapp.data.data_source.local.source.ImageFileDataSource
import com.example.noteapp.data.utils.ImageUtils
import com.example.noteapp.domain.repository.IImageRepository
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepositoryImpl @Inject constructor(
   private val imageFileDataSource: ImageFileDataSource
) : IImageRepository {
    override suspend fun saveImage(uri: Uri, fileName: String): String {
        return imageFileDataSource.saveImage( uri, fileName)
    }

    override suspend fun getImage(fileName: String): File? {
        return imageFileDataSource.getImage(fileName)
    }

    override suspend fun deleteImage(fileName: String): Boolean {
        return imageFileDataSource.deleteImage(fileName)
    }
}