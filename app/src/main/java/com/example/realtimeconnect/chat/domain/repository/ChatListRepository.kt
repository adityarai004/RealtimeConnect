package com.example.realtimeconnect.chat.domain.repository

import com.example.realtimeconnect.chat.data.model.ChatListResponseDTO
import com.example.realtimeconnect.core.Resource
import kotlinx.coroutines.flow.Flow

interface ChatListRepository {
    suspend fun fetchUserIds(page: Int, perPage: Int): Flow<Resource<ChatListResponseDTO>>
}