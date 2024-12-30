package com.example.realtimeconnect.chat.presentation.groupchat.state

data class GroupChatState(
    val messageValue: String = "",
    val otherGuyTyping: Boolean = false
)