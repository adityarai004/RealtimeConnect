package com.example.realtimeconnect.features.auth.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDTO(
    @SerialName("data")
    val `data`: Data? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("status")
    val status: Boolean? = null
)

@Serializable
data class Data(
    @SerialName("authToken")
    val authToken: String? = null,
    @SerialName("userId")
    val userId: String? = null
)