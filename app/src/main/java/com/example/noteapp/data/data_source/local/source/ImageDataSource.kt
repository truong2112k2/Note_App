package com.example.noteapp.data.data_source.local.source

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.noteapp.common.Constants
import com.example.noteapp.data.data_source.local.repository.IImageDataSourceRepository
import com.example.noteapp.data.data_source.local.utils.ImageUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ImageDataSource @Inject constructor(@ApplicationContext val context: Context) : IImageDataSourceRepository {

 override   suspend fun saveImage(uri: Uri, fileName: String): String {

        return withContext(Dispatchers.Default) {
            try {
                ImageUtils.saveImageToInternalStorage(context, uri, fileName)

            } catch (e: Exception) {
                Log.d(Constants.ERROR, "ImageFileDataSource-saveImage: ${e.message}")
                ""
            }
        }

    }

    override  suspend fun getImage(fileName: String): File? {
        return withContext(Dispatchers.Default) {
            try {
                ImageUtils.readImageFile(context, fileName)
            } catch (e: Exception) {
                Log.d(Constants.ERROR, "ImageFileDataSource-getImage: ${e.message}")

                null
            }

        }
    }

    override  suspend fun deleteImage(fileName: String): Boolean {
        return withContext(Dispatchers.Default) {
            try {
                ImageUtils.deleteImageFile(context, fileName)
            } catch (e: Exception) {
                Log.d(Constants.ERROR, "ImageFileDataSource-deleteImage: ${e.message}")

                false
            }
        }
    }
}