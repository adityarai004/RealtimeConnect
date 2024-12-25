package com.example.realtimeconnect.auth.data.model

import kotlinx.serialization.SerialName

data class CommonResponse<T>(
    @SerialName("statusType")
    val statusType: Boolean? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("data")
    val data: T? = null
)