package com.example.realtimeconnect.features.chat.domain.repository

import com.example.realtimeconnect.core.Resource
import com.example.realtimeconnect.features.chat.data.model.GroupMessageDTO
import com.example.realtimeconnect.features.chat.data.model.MessageDTO
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    suspend fun getMessages(receiverId: String): Flow<Resource<List<MessageDTO>?>>
    suspend fun getGroupMessages(page: Int,perPage: Int, groupId: String): Flow<Resource<List<GroupMessageDTO?>?>>
}