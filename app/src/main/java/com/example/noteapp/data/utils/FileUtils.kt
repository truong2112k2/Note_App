package com.example.noteapp.data.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object FileUtils {
    fun saveImageToInternalStorage(context: Context, uri: Uri, fileName: String): String {
        return try {
            val imageDir = File(context.filesDir, "image").apply { if (!exists()) mkdir() }
            val outputFile = File(imageDir, fileName)

            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(outputFile).use { output ->
                    input.copyTo(output)
                }
            }
            fileName
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun readImageFile(context: Context, fileName: String): File? {
        val file = File(context.filesDir, "image/$fileName")
        return if (file.exists()) file else null
    }

    fun deleteImageFile(context: Context, fileName: String): Boolean {
        val file = File(context.filesDir, "image/$fileName")
        return file.delete()
    }
}