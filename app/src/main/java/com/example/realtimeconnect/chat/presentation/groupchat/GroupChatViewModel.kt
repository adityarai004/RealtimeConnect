package com.example.realtimeconnect.chat.presentation.groupchat

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimeconnect.chat.data.model.GroupMessageDTO
import com.example.realtimeconnect.chat.data.source.socket.SocketDataSource
import com.example.realtimeconnect.chat.domain.usecase.GetGroupMessagesUseCase
import com.example.realtimeconnect.chat.presentation.chatting.state.ChattingScreenEvents
import com.example.realtimeconnect.chat.presentation.chatting.state.ChattingState
import com.example.realtimeconnect.chat.presentation.groupchat.state.GroupChatState
import com.example.realtimeconnect.core.Resource
import com.example.realtimeconnect.core.constants.SharedPrefsConstants
import com.example.realtimeconnect.core.datastore.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GroupChatViewModel @Inject constructor(private val getGroupMessagesUseCase: GetGroupMessagesUseCase, private val dataStoreHelper: DataStoreHelper) : ViewModel() {
    private val _state = MutableStateFlow(GroupChatState())
    val groupChatState = _state.asStateFlow()

    private val _messages: MutableStateFlow<List<GroupMessageDTO?>> = MutableStateFlow(emptyList())
    val messages = _messages.asStateFlow()

    init {
        viewModelScope.launch (Dispatchers.IO){
            val userId = dataStoreHelper.getString(SharedPrefsConstants.USERID)
            _state.update {
                it.copy(myUserId = userId)
            }
        }
    }
    private val sockets = SocketDataSource()
    fun handleEvents(event: ChattingScreenEvents) {
        when (event) {
            is ChattingScreenEvents.OnSend -> { /* TODO: Handle Sending Message */ }
            is ChattingScreenEvents.OnTextChange -> { /* TODO: Handle Typinh */ }
        }
    }

    suspend fun fetchGroupChats(groupId: String){
        withContext(context = Dispatchers.IO){
            getGroupMessagesUseCase(page = 1, perPage = 10, groupId = groupId).collect{ response ->
                when(response){
                    is Resource.Error -> Log.d("GroupChatViewModel", "Error while fetching group chat ${response.error}")
                    Resource.Loading -> Log.d("GroupChatViewModel", "Loading Group Messages")
                    is Resource.Success -> {
                        _messages.update {
                            it.toMutableList().apply {
                                addAll(response.data ?: emptyList())
                            }
                        }
                    }
                }
            }
        }
    }


}