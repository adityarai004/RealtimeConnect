package com.example.realtimeconnect.features.chat.presentation.chatting.state

sealed class ChattingScreenEvents{
    data class OnTextChange(val newValue: String): ChattingScreenEvents()
    data object OnSend: ChattingScreenEvents()
}