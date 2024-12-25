package com.example.realtimeconnect.chat.domain.repository

import com.example.realtimeconnect.chat.data.model.MessageDTO
import com.example.realtimeconnect.core.Resource
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    suspend fun getMessages(receiverId: String): Flow<Resource<List<MessageDTO>?>>
}