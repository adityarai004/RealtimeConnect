package com.example.realtimeconnect.features.chat.presentation.groupchat.state


sealed class GroupChatEvents{
    data class OnTextChange(val newValue: String): GroupChatEvents()
    data object OnSend: GroupChatEvents()
    data object OnDismissDialog: GroupChatEvents()
    data class OnTapAdd(val groupId: String): GroupChatEvents()
    data class OnAddMember(val groupId: String, val userId: String): GroupChatEvents()
}