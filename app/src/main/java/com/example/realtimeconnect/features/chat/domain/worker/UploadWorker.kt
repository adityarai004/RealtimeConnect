package com.example.realtimeconnect.features.chat.domain.worker

import android.content.Context
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.impl.model.Dependency
import com.example.realtimeconnect.features.chat.domain.repository.MessagesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File
import java.net.URI
import javax.inject.Inject

@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val messagesRepository: MessagesRepository
) : CoroutineWorker(appContext, workerParameters) {
    override suspend fun doWork(): Result {
        val receiverId = inputData.getString("receiverId")
        val filePath = inputData.getString("filePath")
        val mimeType = inputData.getString("mimeType")
        val fileName = inputData.getString("fileName")
        val content = inputData.getString("content")
        val messageRowId = inputData.getLong("messageRowId", -1)
        val mediaRowId = inputData.getLong("mediaRowId", -1)

        if ((mediaRowId == -1L) || (messageRowId == -1L) || (receiverId == null) || (filePath == null) || (content == null) || (mimeType == null) || (fileName == null)) {
            return Result.failure()
        }
        val response = messagesRepository.sendMediaMessageAndUpdateLocal(
            receiverId,
            content,
            filePath, fileName, mimeType, messageRowId, mediaRowId
        )
        if (response) {
            return Result.success()
        }
        return Result.failure()
    }
}