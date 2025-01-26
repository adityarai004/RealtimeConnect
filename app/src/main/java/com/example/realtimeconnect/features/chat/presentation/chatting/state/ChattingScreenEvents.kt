package com.example.realtimeconnect.features.chat.presentation.chatting.state

import android.net.Uri

sealed class ChattingScreenEvents{
    data class OnTextChange(val newValue: String): ChattingScreenEvents()
    data class OnSendMedia(val uri: Uri): ChattingScreenEvents()
    data object OnSend: ChattingScreenEvents()
}