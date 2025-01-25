package com.example.realtimeconnect.features.chat.domain.repository

import com.example.realtimeconnect.core.Resource
import com.example.realtimeconnect.features.chat.data.model.GroupMessageDTO
import com.example.realtimeconnect.features.chat.data.model.MessageDTO
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    suspend fun getGroupMessages(page: Int,perPage: Int, groupId: String): Flow<Resource<List<GroupMessageDTO?>?>>
    suspend fun getLocalMessages(senderId: String, receivedId: String): Flow<List<MessageDTO>>
    suspend fun syncRemoteServer(senderId: String, receiverId: String): Flow<Unit>
    suspend fun sendMessage(message: String, senderId: String, receiverId: String): Flow<Unit>
    suspend fun receiveMessage()
    suspend fun messageSeen(senderId: String, receiverId: String)
    suspend fun messageStatusUpdate()
}