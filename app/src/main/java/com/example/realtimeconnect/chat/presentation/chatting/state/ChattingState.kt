package com.example.realtimeconnect.chat.presentation.chatting.state

data class ChattingState(
    val messageValue: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    val otherGuyTyping: Boolean = false
)