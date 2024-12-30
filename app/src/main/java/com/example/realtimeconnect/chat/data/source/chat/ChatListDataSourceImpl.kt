package com.example.realtimeconnect.chat.data.source.chat

import com.example.realtimeconnect.chat.data.model.ChatListResponseDTO
import com.example.realtimeconnect.core.constants.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class ChatListDataSourceImpl @Inject constructor(private val httpClient: HttpClient) :
    ChatListDataSource {
    override suspend fun fetchChatList(page: Int, perPage: Int): ChatListResponseDTO {
        val response = httpClient.get(NetworkConstants.CHATS){
            url {
                parameter(key = "page", value = page)
                parameter(key = "perPage", value = perPage)
            }
        }.body<ChatListResponseDTO>()
        return response;
    }
}