package com.example.realtimeconnect.features.chat.domain.repository

import com.example.realtimeconnect.core.Resource
import com.example.realtimeconnect.features.chat.data.model.ChatListResponseDTO
import com.example.realtimeconnect.features.chat.data.model.JoinGroupResponseDTO
import kotlinx.coroutines.flow.Flow

interface ChatListRepository {
    suspend fun fetchChats(page: Int, perPage: Int): Flow<Resource<ChatListResponseDTO>>
    suspend fun fetchNonGroupUsers(page: Int, perPage: Int, groupId: String): Flow<Resource<ChatListResponseDTO>>
    suspend fun addUserToGroup(groupId: String, userId: String): Flow<Resource<JoinGroupResponseDTO>>
}