package com.example.realtimeconnect.features.chat.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DMResponseDTO(
    @SerialName("message")
    var message: String?,
    @SerialName("messageData")
    var messageData: MessageData?,
    @SerialName("success")
    var success: Boolean?
)


@Serializable
data class MessageData(
    @SerialName("messages")
    var messages: List<Message?>?,
    @SerialName("total")
    var total: Int?
)



@Serializable
data class Message(
    @SerialName("createdAt")
    var createdAt: String,
    @SerialName("id")
    var id: String,
    @SerialName("messageContent")
    var messageContent: String,
    @SerialName("receiverId")
    var receiverId: String,
    @SerialName("senderId")
    var senderId: String,
    @SerialName("status")
    var status: String?,
    @SerialName("contentType")
    val contentType: String = "text"
)
