package com.example.realtimeconnect.core.constants

import com.example.realtimeconnect.chat.data.model.GroupChatData
import com.example.realtimeconnect.chat.data.model.GroupData
import kotlinx.serialization.Serializable

@Serializable
object LoginNavigation

@Serializable
object SignUpNavigation

@Serializable
object HomeScreenNavigation

@Serializable
data class ChattingNavigation(val userId: String)

@Serializable
data class GroupChatNavigation(val groupId: String, val groupName: String)