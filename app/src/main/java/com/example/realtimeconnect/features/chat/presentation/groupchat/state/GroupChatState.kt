package com.example.realtimeconnect.features.chat.presentation.groupchat.state

data class GroupChatState(
    val messageValue: String = "",
    val otherGuyTyping: Boolean = false,
    val myUserId: String = "",
    val users: List<String?> = emptyList(),
    val isDialogOpen: Boolean = false,
    val showToast: Boolean = false,
    val toastMessage: String = "",
)