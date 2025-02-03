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
    var total: Int?,
    @SerialName("latestStatus")
    val latestStatus: String?
)
