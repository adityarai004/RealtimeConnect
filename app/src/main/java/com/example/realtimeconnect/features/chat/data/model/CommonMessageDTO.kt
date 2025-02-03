package com.example.realtimeconnect.features.chat.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Message(
    @SerialName("contentType")
    val contentType: String?,
    @SerialName("createdAt")
    val createdAt: String?,
    @SerialName("id")
    val id: String?,
    @SerialName("messageContent")
    val messageContent: String?,
    @SerialName("receiverId")
    val receiverId: String?,
    @SerialName("senderId")
    val senderId: String?,
    @SerialName("status")
    val status: String?,
    @SerialName("userId")
    val userId: String?
)