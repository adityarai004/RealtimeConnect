package com.example.realtimeconnect.features.chat.data.source.messages

import com.example.realtimeconnect.core.constants.NetworkConstants
import com.example.realtimeconnect.core.constants.NetworkConstants.DMS
import com.example.realtimeconnect.features.chat.data.model.DMResponseDTO
import com.example.realtimeconnect.features.chat.data.model.GroupChatListResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class MessagesDataSourceImpl @Inject constructor(private val httpClient: HttpClient) :
    MessagesDataSource {
    override suspend fun getMessages(receiverId: String): DMResponseDTO {
        val response = httpClient.get(DMS){
            parameter("receiverId", receiverId)
        }.body<DMResponseDTO>()

        return response
    }

    override suspend fun getGroupMessages(page: Int, perPage: Int, groupId: String): GroupChatListResponseDTO {
        val response = httpClient.get(NetworkConstants.GROUP_MESSAGES){
            url{
                parameter(key = "page", value = page)
                parameter(key = "perPage", value = perPage)
                parameter(key = "groupId", value = groupId)
            }
        }.body<GroupChatListResponseDTO>()
        return response
    }
}