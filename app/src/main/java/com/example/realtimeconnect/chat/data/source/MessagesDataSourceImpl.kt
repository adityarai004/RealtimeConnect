package com.example.realtimeconnect.chat.data.source

import com.example.realtimeconnect.chat.data.model.DMResponseDTO
import com.example.realtimeconnect.core.constants.NetworkConstants.DMS
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class MessagesDataSourceImpl @Inject constructor(private val httpClient: HttpClient) : MessagesDataSource {
    override suspend fun getMessages(receiverId: String): DMResponseDTO {
        val response = httpClient.get(DMS){
            parameter("receiverId", receiverId)
        }.body<DMResponseDTO>()

        return response
    }
}