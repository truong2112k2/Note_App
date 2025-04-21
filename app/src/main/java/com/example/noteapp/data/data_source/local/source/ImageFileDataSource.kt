package com.example.noteapp.data.data_source.local.source

import android.content.Context
import android.net.Uri
import com.example.noteapp.data.utils.ImageUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class ImageFileDataSource @Inject constructor ( @ApplicationContext val context: Context) {

     suspend fun saveImage(uri: Uri, fileName: String): String {
        return ImageUtils.saveImageToInternalStorage(  context, uri, fileName)
    }

     suspend fun getImage(fileName: String): File? {
        return ImageUtils.readImageFile(context, fileName)
    }

     suspend fun deleteImage(fileName: String): Boolean {
        return ImageUtils.deleteImageFile(context, fileName)
    }
}