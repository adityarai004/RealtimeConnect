package com.example.realtimeconnect.chat.data.source.messages

import com.example.realtimeconnect.chat.data.model.DMResponseDTO
import com.example.realtimeconnect.chat.data.model.GroupChatListResponseDTO

interface MessagesDataSource {
    suspend fun getMessages(receiverId: String): DMResponseDTO
    suspend fun getGroupMessages(page: Int, perPage: Int, groupId: String): GroupChatListResponseDTO

}