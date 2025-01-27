package com.example.realtimeconnect.features.chat.domain.usecase

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.net.toFile
import com.example.realtimeconnect.features.chat.domain.repository.MessagesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class UploadMediaUseCase @Inject constructor(
    private val repository: MessagesRepository,
    @ApplicationContext private val context: Context
) {

    suspend operator fun invoke(senderId: String, receiverId: String, content: String, uri: Uri) =
        repository.sendFile(senderId, receiverId, content, createTempFileFromUri(uri))


    private fun createTempFileFromUri(uri: Uri): File {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: throw IllegalArgumentException("Cannot open URI")

            val fileExtension = context.contentResolver.getType(uri)
                ?.substringAfter("/")
                ?: "jpg" // Default to jpg if can't determine

            val tempFile = File(context.cacheDir, "tempFile1.${fileExtension}")
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            return tempFile
        }
        else {
            val file = uri.toFile()
            return file
        }
    }

}