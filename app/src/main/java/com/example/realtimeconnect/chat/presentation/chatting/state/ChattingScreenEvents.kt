package com.example.realtimeconnect.chat.presentation.chatting.state

sealed class ChattingScreenEvents{
    data class OnTextChange(val newValue: String): ChattingScreenEvents()
    data object OnSend: ChattingScreenEvents()
}