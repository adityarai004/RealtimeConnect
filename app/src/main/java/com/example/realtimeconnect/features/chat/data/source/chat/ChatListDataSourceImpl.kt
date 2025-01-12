package com.example.realtimeconnect.features.chat.data.source.chat

import com.example.realtimeconnect.core.constants.NetworkConstants
import com.example.realtimeconnect.features.chat.data.model.ChatListResponseDTO
import com.example.realtimeconnect.features.chat.data.model.JoinGroupResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
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

    override suspend fun fetchNonGroupUsers(page: Int, perPage: Int, groupId: String): ChatListResponseDTO {
        val response = httpClient.get(NetworkConstants.NON_GROUP_USERS){
            url {
                parameter(key = "page", value = page)
                parameter(key = "perPage", value = perPage)
                parameter(key = "groupId", value = groupId)
            }
        }.body<ChatListResponseDTO>()
        return response;
    }

    override suspend fun joinGroup(userId: String, groupId: String): JoinGroupResponseDTO {
        val response = httpClient.post(NetworkConstants.JOIN_GROUP) {
            url {
                setBody(mapOf("userId" to userId, "groupId" to groupId))
            }
        }.body<JoinGroupResponseDTO>()
        return response
    }
}