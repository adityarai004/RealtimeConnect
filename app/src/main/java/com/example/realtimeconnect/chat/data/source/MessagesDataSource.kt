package com.example.realtimeconnect.chat.data.source

import com.example.realtimeconnect.chat.data.model.DMResponseDTO

interface MessagesDataSource {
    suspend fun getMessages(receiverId: String): DMResponseDTO
}