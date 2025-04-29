package com.example.noteapp.data.data_source.local.source

import android.content.Context
import android.net.Uri
import com.example.noteapp.data.data_source.local.utils.ImageUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ImageFileDataSource @Inject constructor(@ApplicationContext val context: Context) {

    suspend fun saveImage(uri: Uri, fileName: String): String {

        return withContext(Dispatchers.Default){
            try{
                ImageUtils.saveImageToInternalStorage(context, uri, fileName)

            }catch (e: Exception){
                ""
            }
        }

    }

    suspend fun getImage(fileName: String): File? {
        return withContext(Dispatchers.Default){
            try {
                ImageUtils.readImageFile(context, fileName)
            }catch (e: Exception){
                null
            }

        }
    }

    suspend fun deleteImage(fileName: String): Boolean {
        return withContext(Dispatchers.Default){
            try{
                ImageUtils.deleteImageFile(context, fileName)
            }catch (e: Exception){
                false
            }
        }
    }
}