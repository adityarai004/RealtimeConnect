package com.example.realtimeconnect.features.chat.domain.usecase

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.webkit.MimeTypeMap
import androidx.core.net.toFile
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.realtimeconnect.features.chat.domain.repository.MessagesRepository
import com.example.realtimeconnect.features.chat.domain.worker.UploadWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject


class UploadMediaUseCase @Inject constructor(
    private val repository: MessagesRepository,
    @ApplicationContext private val context: Context,
    private val workManager: WorkManager
) {

    suspend operator fun invoke(senderId: String, receiverId: String, content: String, uri: Uri) {
        val tempFile = createTempFileFromUri(uri)
        val pairs = repository.insertMediaMessageToLocal(senderId, receiverId, content, tempFile)

        if (pairs.first == null || pairs.second == null) {
            return
        }
        Log.d(
            "UploadMediaUseCase",
            "invoke: $pairs ${tempFile.path} ${tempFile.name} ${getMimeType(tempFile.path)}"
        )
        val data: Data.Builder = Data.Builder()
        data.putAll(
            mapOf(
                "receiverId" to receiverId,
                "fileName" to tempFile.name,
                "filePath" to tempFile.path,
                "mimeType" to getMimeType(tempFile.path),
                "content" to content,
                "messageRowId" to pairs.first,
                "mediaRowId" to pairs.second
            )
        )
        val uploadWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setInputData(data.build())
            .build()
        workManager.enqueue(uploadWorkRequest)
    }


    private fun createTempFileFromUri(uri: Uri): File {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
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
        } else {
            val file = uri.toFile()
            return file
        }
    }

    fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

}