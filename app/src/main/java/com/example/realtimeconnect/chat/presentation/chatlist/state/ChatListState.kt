package com.example.realtimeconnect.chat.presentation.chatlist.state

import com.example.realtimeconnect.chat.data.model.GroupData

data class ChatListState(
    val loading: Boolean = true,
    val people: MutableList<String> = mutableListOf(),
    val groups: MutableList<GroupData> = mutableListOf(),
    val page: Int = 1,
    val perPage: Int = 10,
    val hasError: Boolean = false,
    val errorString: String = ""
)