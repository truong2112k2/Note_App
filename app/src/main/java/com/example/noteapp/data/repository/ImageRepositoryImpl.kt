package com.example.noteapp.data.repository

import android.net.Uri
import com.example.noteapp.data.data_source.local.source.IImageDataSource
import com.example.noteapp.domain.repository.IImageRepository
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepositoryImpl @Inject constructor(
 private val imageFileDataSource: IImageDataSource
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