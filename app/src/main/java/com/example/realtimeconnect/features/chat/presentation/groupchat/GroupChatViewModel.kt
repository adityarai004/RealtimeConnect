package com.example.realtimeconnect.features.chat.presentation.groupchat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimeconnect.core.Resource
import com.example.realtimeconnect.core.constants.SharedPrefsConstants
import com.example.realtimeconnect.core.datastore.DataStoreHelper
import com.example.realtimeconnect.features.chat.data.model.GroupMessageDTO
import com.example.realtimeconnect.features.chat.data.source.socket.SocketDataSource
import com.example.realtimeconnect.features.chat.domain.usecase.FetchNonGroupUsersUseCase
import com.example.realtimeconnect.features.chat.domain.usecase.GetGroupMessagesUseCase
import com.example.realtimeconnect.features.chat.domain.usecase.JoinGroupUseCase
import com.example.realtimeconnect.features.chat.presentation.groupchat.state.GroupChatEvents
import com.example.realtimeconnect.features.chat.presentation.groupchat.state.GroupChatState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupChatViewModel @Inject constructor(
    private val getGroupMessagesUseCase: GetGroupMessagesUseCase,
    private val getUsersUseCase: FetchNonGroupUsersUseCase,
    private val dataStoreHelper: DataStoreHelper,
    private val joinGroupUseCase: JoinGroupUseCase
) : ViewModel() {
    companion object {
        const val TAG = "GroupChatViewModel"
    }

    private val _state = MutableStateFlow(GroupChatState())
    val groupChatState = _state.asStateFlow()

    private val _messages: MutableStateFlow<List<GroupMessageDTO?>> = MutableStateFlow(emptyList())
    val messages = _messages.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = dataStoreHelper.getString(SharedPrefsConstants.USERID)
            _state.update {
                it.copy(myUserId = userId)
            }
        }
    }

    private val sockets = SocketDataSource()
    fun handleEvents(event: GroupChatEvents) {
        when (event) {
            is GroupChatEvents.OnSend -> { /* TODO: Handle Sending Message */
            }

            is GroupChatEvents.OnTextChange -> { /* TODO: Handle Typinh */
            }

            is GroupChatEvents.OnTapAdd -> {
                fetchUsersToAdd(event.groupId)
                _state.update {
                    it.copy(isDialogOpen = true)
                }
            }

            is GroupChatEvents.OnDismissDialog -> {
                _state.update {
                    it.copy(isDialogOpen = false)
                }
            }

            is GroupChatEvents.OnAddMember -> {
                viewModelScope.launch {
                    joinGroupUseCase(event.groupId, event.userId).collect{
                        when(it){
                            is Resource.Error -> Log.d(TAG, "Error while adding user to group ${it.error}")
                            Resource.Loading -> Log.d(TAG, "Loading")
                            is Resource.Success -> {
                                Log.d(TAG, "User added to group")
                                fetchUsersToAdd(event.groupId)
                            }
                        }
                    }
                }
            }
        }
    }

    fun fetchGroupChats(groupId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getGroupMessagesUseCase(page = 1, perPage = 10, groupId = groupId).collect { response ->
                when (response) {
                    is Resource.Error -> Log.d(
                        "GroupChatViewModel",
                        "Error while fetching group chat ${response.error}"
                    )

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

    private fun fetchUsersToAdd(groupId: String) {
        viewModelScope.launch {
            getUsersUseCase(groupId = groupId).collect { response ->
                when (response) {
                    is Resource.Error -> Log.d(
                        TAG,
                        "Error while fetching non group users ${response.error}"
                    )

                    Resource.Loading -> Log.d(TAG, "Loading Non Group users")
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                users = response.data.chatListData?.userIds ?: emptyList()
                            )
                        }
                        Log.d(TAG,"Current users ${_state.value.users.size}")
                    }
                }
            }
        }
    }
}