package com.example.realtimeconnect.features.chat.presentation.chatting.state

data class ChattingState(
    val messageValue: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val otherGuyTyping: Boolean = false
)