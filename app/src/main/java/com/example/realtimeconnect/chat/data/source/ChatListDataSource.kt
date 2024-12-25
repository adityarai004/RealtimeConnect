package com.example.realtimeconnect.chat.data.source

import com.example.realtimeconnect.chat.data.model.ChatListResponseDTO

interface ChatListDataSource {
    suspend fun fetchChatList(page: Int, perPage: Int):ChatListResponseDTO
}