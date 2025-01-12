package com.example.realtimeconnect.features.chat.data.source.chat

import com.example.realtimeconnect.features.chat.data.model.ChatListResponseDTO
import com.example.realtimeconnect.features.chat.data.model.JoinGroupResponseDTO

interface ChatListDataSource {
    suspend fun fetchChatList(page: Int, perPage: Int): ChatListResponseDTO
    suspend fun fetchNonGroupUsers(page: Int, perPage: Int, groupId: String): ChatListResponseDTO
    suspend fun joinGroup(userId: String, groupId: String): JoinGroupResponseDTO
}