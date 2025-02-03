package com.example.realtimeconnect.features.chat.domain.repository

import com.example.realtimeconnect.core.Resource
import com.example.realtimeconnect.features.chat.data.model.GroupMessageDTO
import com.example.realtimeconnect.features.chat.data.model.MessageDTO
import kotlinx.coroutines.flow.Flow
import java.io.File

interface MessagesRepository {
    suspend fun getGroupMessages(
        page: Int,
        perPage: Int,
        groupId: String
    ): Flow<Resource<List<GroupMessageDTO?>?>>

    suspend fun getLocalMessages(senderId: String, receivedId: String): Flow<List<MessageDTO>>
    suspend fun syncRemoteServer(senderId: String, receiverId: String): Flow<Unit>
    suspend fun sendMessage(message: String, senderId: String, receiverId: String): Flow<Unit>
    suspend fun receiveMessage()
    suspend fun messageSeen(senderId: String, receiverId: String)
    suspend fun messageStatusUpdate()
    suspend fun insertMediaMessageToLocal(
        senderId: String,
        receiverId: String,
        content: String,
        file: File
    ): Pair<Long?, Long?>

    suspend fun sendMediaMessageAndUpdateLocal(
        receiverId: String, content: String,
        filePath: String,
        fileName: String,
        mimeType: String, messageRowId: Long, mediaRowId: Long,
    ): Boolean
}