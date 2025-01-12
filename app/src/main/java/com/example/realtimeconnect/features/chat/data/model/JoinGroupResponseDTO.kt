package com.example.realtimeconnect.features.chat.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class JoinGroupResponseDTO(
    @SerialName("status") var status: Boolean? = null,
    @SerialName("message") var message: String? = null)