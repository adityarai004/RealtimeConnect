package com.example.realtimeconnect.chat.presentation.chatting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimeconnect.chat.data.model.MessageDTO
import com.example.realtimeconnect.chat.data.source.SocketDataSource
import com.example.realtimeconnect.chat.domain.usecase.GetMessagesUseCase
import com.example.realtimeconnect.chat.presentation.chatting.state.ChattingScreenEvents
import com.example.realtimeconnect.chat.presentation.chatting.state.ChattingState
import com.example.realtimeconnect.core.Resource
import com.example.realtimeconnect.core.constants.SharedPrefsConstants
import com.example.realtimeconnect.core.datastore.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(
    private val dataStoreHelper: DataStoreHelper,
    private val getMessagesUseCase: GetMessagesUseCase
) :
    ViewModel() {
    private val _state: MutableStateFlow<ChattingState> = MutableStateFlow(ChattingState())
    val chattingState = _state.asStateFlow()
    private val _messageState: MutableStateFlow<List<MessageDTO>> = MutableStateFlow(emptyList())
    val messageState = _messageState.asStateFlow()

    //Global context so that the next job should be on the same thread
    private val context = Dispatchers.Default
    private var debounceJob: Job? = null

    private val sockets: SocketDataSource = SocketDataSource()

    fun handleEvents(event: ChattingScreenEvents) {
        when (event) {
            is ChattingScreenEvents.OnSend -> {
                if (chattingState.value.messageValue.isNotEmpty()) {
                    sockets.sendMessage(
                        mapOf(
                            "msg" to chattingState.value.messageValue,
                            "receiverId" to chattingState.value.receiverId,
                            "senderId" to chattingState.value.senderId,
                        )
                    ) {
                        if (it["sent"] == 1) {
                            _messageState.update { msgState ->
                                msgState.toMutableList().apply {
                                    add(
                                        MessageDTO(
                                            content = chattingState.value.messageValue,
                                            senderId = chattingState.value.senderId,
                                            receiverId = chattingState.value.receiverId,
                                            timestamp = (it["timestamp"] as? String)?.substring(
                                                11,
                                                16
                                            ),
                                            messageId = it["msgId"] as? String,
                                            status = "sent"
                                        )
                                    )
                                }
                            }
                            _state.update { state ->
                                state.copy(messageValue = "")
                            }
                        }
                    }
                }
            }

            is ChattingScreenEvents.OnTextChange -> {
                _state.update {
                    it.copy(messageValue = event.newValue)
                }
                sockets.sendEvent(
                    "typing",
                    mapOf("user" to chattingState.value.receiverId, "typing" to 1)
                )
            }
        }
    }

    fun connect(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val senderId = dataStoreHelper.getString(SharedPrefsConstants.USERID)
            _state.update {
                it.copy(receiverId = userId, senderId = senderId)
            }
            getMessages(userId)
            sockets.connectSocket(senderId, userId)
            sockets.onMessage("receive-message") {
                Log.d("Receive Message", it.toString())
                _messageState.update { state ->
                    state.toMutableList().apply {
                        add(
                            MessageDTO(
                                content = it["msg"] as? String ?: "",
                                receiverId = it["receiverId"] as? String,
                                timestamp = (it["timestamp"] as String).apply {
                                    this.substring(11, 16)
                                },
                                messageId = it["msgId"] as? String,
                            )
                        )
                    }
                }
            }
            sockets.onMessage("typing") {
                debounceJob?.cancel()
                _state.update { currState ->
                    currState.copy(otherGuyTyping = it["typing"] == 1)
                }
                debounceJob = CoroutineScope(context).launch {
                    delay(2000L)
                    _state.update { currState ->
                        currState.copy(otherGuyTyping = false)
                    }
                }
            }
            sockets.onMessage("message-status") {
                Log.d("MessageStatus", "Message Status ${it["id"]}")
                val msgId = it["id"] as String
                val status = it["status"] as String

                _messageState.update { currList ->
                    currList.map { msg ->
                        if (msg.messageId == msgId) {
                            msg.copy(status = status)
                        } else {
                            msg
                        }
                    }
                }
            }
            sockets.onMessage("messages-seen"){
                _messageState.update { currList ->
                    currList.map { msg ->
                        msg.copy(status = "seen")
                    }
                }
            }
        }
    }

    private fun getMessages(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getMessagesUseCase.invoke(userId).collect { response ->
                when (response) {
                    is Resource.Error -> {

                    }

                    Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        _messageState.update { msgState ->
                            msgState.toMutableList().apply {
                                addAll(response.data ?: emptyList())
                            }
                        }
                    }
                }
            }
        }
    }
}