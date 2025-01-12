package com.example.realtimeconnect.features.chat.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatListResponseDTO(
    @SerialName("status") var status: Boolean? = null,
    @SerialName("message") var message: String? = null,
    @SerialName("data") var chatListData: ChatListData? = ChatListData()
)


@Serializable
data class ChatListData(
    @SerialName("userIds") var userIds: ArrayList<String> = arrayListOf(),
    @SerialName("groups") var groups: ArrayList<GroupData>? = null
)

@Serializable
data class GroupData(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null
)