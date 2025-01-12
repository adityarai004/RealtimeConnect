package com.example.realtimeconnect.features.chat.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupChatListResponseDTO(
    @SerialName("data")
    val `data`: List<GroupChatData?>?,
    @SerialName("message")
    val message: String?,
    @SerialName("status")
    val status: Boolean?
)


@Serializable
data class GroupChatData(
    @SerialName("content")
    val content: String?,
    @SerialName("contentType")
    val contentType: String?,
    @SerialName("createdAt")
    val createdAt: String?,
    @SerialName("groupId")
    val groupId: String?,
    @SerialName("id")
    val id: String?,
    @SerialName("isActive")
    val isActive: Boolean?,
    @SerialName("messageType")
    val messageType: String?,
    @SerialName("senderId")
    val senderId: String?,
    @SerialName("status")
    val status: String?
)