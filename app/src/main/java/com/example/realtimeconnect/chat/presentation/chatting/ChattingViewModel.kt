package com.example.realtimeconnect.chat.presentation.chatting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimeconnect.chat.data.model.MessageDTO
import com.example.realtimeconnect.chat.data.source.socket.SocketDataSource
import com.example.realtimeconnect.chat.domain.usecase.GetMessagesUseCase
import com.example.realtimeconnect.chat.presentation.chatting.state.ChattingScreenEvents
import com.example.realtimeconnect.chat.presentation.chatting.state.ChattingState
import com.example.realtimeconnect.core.Resource
import com.example.realtimeconnect.core.constants.SharedPrefsConstants
import com.example.realtimeconnect.core.datastore.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(
    private val dataStoreHelper: DataStoreHelper,
    private val getMessagesUseCase: GetMessagesUseCase
) : ViewModel() {

    // State management
    private val _state = MutableStateFlow(ChattingState())
    val chattingState = _state.asStateFlow()

    private val _messageState = MutableStateFlow<List<MessageDTO>>(emptyList())
    val messageState = _messageState.asStateFlow()

    // Socket instance
    private val sockets = SocketDataSource()

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
        }
    }

    private fun handleSendMessage() {
        if (chattingState.value.messageValue.isNotEmpty()) {
            sockets.sendMessage(
                mapOf(
                    "msg" to chattingState.value.messageValue,
                    "receiverId" to chattingState.value.receiverId,
                    "senderId" to chattingState.value.senderId
                )
            ) { response ->
                if (response["sent"] == 1) {
                    addMessageToState(
                        MessageDTO(
                            content = chattingState.value.messageValue,
                            senderId = chattingState.value.senderId,
                            receiverId = chattingState.value.receiverId,
                            timestamp = (response["timestamp"] as? String)?.substring(11, 16),
                            messageId = response["msgId"] as? String,
                            status = "sent"
                        )
                    )
                    clearMessageInput()
                }
            }
        }
    }

    private fun handleTextChange(newValue: String) {
        _state.update { it.copy(messageValue = newValue) }
        sockets.sendEvent("typing", mapOf("user" to chattingState.value.receiverId, "typing" to 1))
    }

    /**
     * Socket connection and event listeners
     */
    fun connect(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val senderId = dataStoreHelper.getString(SharedPrefsConstants.USERID)
            _state.update { it.copy(receiverId = userId, senderId = senderId) }

            getMessages(userId)
            sockets.connectSocket(senderId, userId)

            setupSocketListeners()
        }
    }

    private fun setupSocketListeners() {
        sockets.onMessage("receive-message") { handleReceivedMessage(it) }
        sockets.onMessage("typing") { handleTypingEvent(it) }
        sockets.onMessage("message-status") { handleMessageStatusUpdate(it) }
        sockets.onMessage("messages-seen") { markMessagesAsSeen() }
    }

    private fun handleReceivedMessage(data: JSONObject) {
        Log.d("Receive Message", data.toString())
        addMessageToState(
            MessageDTO(
                content = data["msg"] as? String ?: "",
                receiverId = data["receiverId"] as? String,
                timestamp = (data["timestamp"] as String).substring(11, 16),
                messageId = data["msgId"] as? String
            )
        )
    }

    private fun handleTypingEvent(data: JSONObject) {
        debounceJob?.cancel()
        _state.update { it.copy(otherGuyTyping = data["typing"] == 1) }
        debounceJob = CoroutineScope(coroutineContext).launch {
            delay(2000L)
            _state.update { it.copy(otherGuyTyping = false) }
        }
    }

    private fun handleMessageStatusUpdate(data: JSONObject) {
        val msgId = data["id"] as String
        val status = data["status"] as String
        _messageState.update { list ->
            list.map { if (it.messageId == msgId) it.copy(status = status) else it }
        }
    }

    private fun markMessagesAsSeen() {
        _messageState.update { list ->
            list.map { it.copy(status = "seen") }
        }
    }

    /**
     * Fetch messages from the backend
     */
    private fun getMessages(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getMessagesUseCase.invoke(userId).collect { response ->
                when (response) {
                    is Resource.Error -> Log.e("GetMessages", "Error: ${response.error}")
                    Resource.Loading -> Log.d("GetMessages", "Loading")
                    is Resource.Success -> {
                        _messageState.update { it + (response.data ?: emptyList()) }
                    }
                }
            }
        }
    }

    /**
     * State management helpers
     */
    private fun addMessageToState(message: MessageDTO) {
        _messageState.update { it + message }
    }

    private fun clearMessageInput() {
        _state.update { it.copy(messageValue = "") }
    }
}
