package com.example.realtimeconnect.features.chat.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatListRequestDTO(
    @SerialName("page")
    val page: Int?,
    @SerialName("perPage")
    val perPage: Int?
)
