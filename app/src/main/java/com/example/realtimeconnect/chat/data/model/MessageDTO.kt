package com.example.realtimeconnect.chat.data.model

data class MessageDTO(
    val isMe: Boolean? = true,
    val content: String? = null,
    val senderId: String? = null,
    val receiverId: String? = null,
    val timestamp: String? = null,
    var status: String? = null,
    val messageId: String? = null
)