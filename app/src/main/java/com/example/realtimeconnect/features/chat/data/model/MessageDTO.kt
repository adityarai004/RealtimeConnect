package com.example.realtimeconnect.features.chat.data.model

data class MessageDTO(
    val content: String? = null,
    val senderId: String? = null,
    val receiverId: String? = null,
    val timestamp: String? = null,
    var status: String? = null,
    val messageId: String? = null,
    val contentType: String = "text",
    val fileUri: String? = null,
    val remoteUrl: String? = null,
    val mediaType: String = "image"
)