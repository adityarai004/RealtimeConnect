package com.example.realtimeconnect.features.auth.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDTO(
    @SerialName("userId")
    val username: String,
    @SerialName("pwd")
    val password: String
)