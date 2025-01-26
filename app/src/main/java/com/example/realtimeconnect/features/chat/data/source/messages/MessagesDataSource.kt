package com.example.realtimeconnect.features.chat.data.source.messages

import android.net.Uri
import com.example.realtimeconnect.features.chat.data.model.DMResponseDTO
import com.example.realtimeconnect.features.chat.data.model.GroupChatListResponseDTO

interface MessagesDataSource {
    suspend fun getMessages(receiverId: String, timestamp: String): DMResponseDTO
    suspend fun getGroupMessages(page: Int, perPage: Int, groupId: String): GroupChatListResponseDTO
    suspend fun uploadMedia(receiverId: String, uri: Uri)

}