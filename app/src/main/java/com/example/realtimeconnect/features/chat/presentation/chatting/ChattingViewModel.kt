package com.example.realtimeconnect.features.chat.presentation.chatting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimeconnect.core.constants.SharedPrefsConstants
import com.example.realtimeconnect.core.datastore.DataStoreHelper
import com.example.realtimeconnect.features.chat.data.model.MessageDTO
import com.example.realtimeconnect.features.chat.data.source.messages.MessagesDataSource
import com.example.realtimeconnect.features.chat.data.source.socket.SocketDataSource
import com.example.realtimeconnect.features.chat.domain.usecase.GetMessagesUseCase
import com.example.realtimeconnect.features.chat.domain.usecase.HandleMessageReceiveUseCase
import com.example.realtimeconnect.features.chat.domain.usecase.HandleMessageSeenUseCase
import com.example.realtimeconnect.features.chat.domain.usecase.MessageStatusUpdateUseCase
import com.example.realtimeconnect.features.chat.domain.usecase.SendMessageUseCase
import com.example.realtimeconnect.features.chat.domain.usecase.SyncRemoteServerUseCase
import com.example.realtimeconnect.features.chat.presentation.chatting.state.ChattingScreenEvents
import com.example.realtimeconnect.features.chat.presentation.chatting.state.ChattingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(
    private val dataStoreHelper: DataStoreHelper,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val syncRemoteServerUseCase: SyncRemoteServerUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val handleMessageReceiveUseCase: HandleMessageReceiveUseCase,
    private val messageStatusUpdateUseCase: MessageStatusUpdateUseCase,
    private val handleMessageSeenUseCase: HandleMessageSeenUseCase,
    private val sockets: SocketDataSource,
    private val messageDataSource: MessagesDataSource
) : ViewModel() {

    // State management
    private val _state = MutableStateFlow(ChattingState())
    val chattingState = _state.asStateFlow()

    private val _messageState = MutableStateFlow<List<MessageDTO>>(emptyList())
    val messageState = _messageState.asStateFlow()

    // Socket instance

    // Coroutine management
    private var debounceJob: Job? = null
    private val coroutineContext = Dispatchers.Default

    /**
     * Handle UI events
     */
    fun handleEvents(event: ChattingScreenEvents) {
        when (event) {
            is ChattingScreenEvents.OnSend -> handleSendMessage()
            is ChattingScreenEvents.OnTextChange -> handleTextChange(event.newValue)
            is ChattingScreenEvents.OnSendMedia -> {
                viewModelScope.launch {
                    messageDataSource.uploadMedia(_state.value.receiverId, event.uri)
                }
            }
        }
    }

    private fun handleSendMessage() {
        if (chattingState.value.messageValue.isNotEmpty()) {
            viewModelScope.launch {
                sendMessageUseCase(
                    message = chattingState.value.messageValue,
                    senderId = chattingState.value.senderId,
                    receiverId = chattingState.value.receiverId
                ).collect{
                    Log.d("Message", "Message sent ${chattingState.value.messageValue}")
                }
            }
            _state.update { it.copy(messageValue = "") }
        }
    }

    private fun handleTextChange(newValue: String) {
        _state.update { it.copy(messageValue = newValue) }
        sockets.sendEvent("typing", mapOf("user" to chattingState.value.receiverId, "typing" to 1))
    }

    /**
     * Socket connection and event listeners
     */
    fun connect(receiverId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val myUserId = dataStoreHelper.getString(SharedPrefsConstants.USERID)
            _state.update { it.copy(receiverId = receiverId, senderId = myUserId) }
            getMessages(chattingState.value.senderId, receiverId)
            sockets.connectSocket(myUserId)
            sockets.sendEvent(
                "message-seen",
                mapOf("senderId" to receiverId, "viewerId" to myUserId)
            )
            setupSocketListeners()
        }
    }

    private fun setupSocketListeners() {
        viewModelScope.launch {
            handleMessageReceiveUseCase()
            handleMessageSeenUseCase(_state.value.senderId, _state.value.receiverId)
            messageStatusUpdateUseCase()
        }
        sockets.onMessage("typing") { handleTypingEvent(it) }
    }

    private fun handleTypingEvent(data: JSONObject) {
        debounceJob?.cancel()
        _state.update { it.copy(otherGuyTyping = data["typing"] == 1) }
        debounceJob = CoroutineScope(coroutineContext).launch {
            delay(2000L)
            _state.update { it.copy(otherGuyTyping = false) }
        }
    }

    /**
     * Fetch messages from the backend
     */
    private fun getMessages(senderId: String, receiverId: String) {
        viewModelScope.launch {
            getMessagesUseCase(senderId, receiverId).collect { response ->
                _messageState.update { response }
            }
        }
        viewModelScope.launch {
            syncRemoteServerUseCase(senderId, receiverId).collect{
                Log.d("Sync", "Synced messages")
            }
        }
    }
}
