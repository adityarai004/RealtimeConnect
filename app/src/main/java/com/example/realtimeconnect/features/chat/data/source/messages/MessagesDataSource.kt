package com.example.realtimeconnect.features.chat.data.source.messages

import com.example.realtimeconnect.features.chat.data.model.DMResponseDTO
import com.example.realtimeconnect.features.chat.data.model.GroupChatListResponseDTO
import com.example.realtimeconnect.features.chat.data.model.UploadMediaResponseDTO
import java.io.File

interface MessagesDataSource {
    suspend fun getMessages(receiverId: String, timestamp: String): DMResponseDTO
    suspend fun getGroupMessages(page: Int, perPage: Int, groupId: String): GroupChatListResponseDTO
    suspend fun uploadMedia(
        receiverId: String,
        content: String,
        filePath: String,
        fileName: String,
        mimeType: String
    ): UploadMediaResponseDTO

}