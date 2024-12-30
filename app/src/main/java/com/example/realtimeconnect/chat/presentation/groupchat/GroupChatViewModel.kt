package com.example.realtimeconnect.chat.presentation.groupchat

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import com.example.realtimeconnect.chat.data.model.GroupMessageDTO
import com.example.realtimeconnect.chat.data.source.socket.SocketDataSource
import com.example.realtimeconnect.chat.domain.usecase.GetGroupMessagesUseCase
import com.example.realtimeconnect.chat.presentation.chatting.state.ChattingScreenEvents
import com.example.realtimeconnect.chat.presentation.chatting.state.ChattingState
import com.example.realtimeconnect.chat.presentation.groupchat.state.GroupChatState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GroupChatViewModel @Inject constructor(private val getGroupMessagesUseCase: GetGroupMessagesUseCase) : ViewModel() {
    private val _state = MutableStateFlow(GroupChatState())
    val groupChatState = _state.asStateFlow()

    private val _messages: MutableStateFlow<List<GroupMessageDTO>> = MutableStateFlow(emptyList())
    val messages = _messages.asStateFlow()

    private val sockets = SocketDataSource()
    fun handleEvents(event: ChattingScreenEvents) {
        when (event) {
            is ChattingScreenEvents.OnSend -> { /* TODO: Handle Sending Message */ }
            is ChattingScreenEvents.OnTextChange -> { /* TODO: Handle Typinh */ }
        }
    }


}