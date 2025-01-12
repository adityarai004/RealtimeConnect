package com.example.realtimeconnect.features.chat.presentation.chatlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimeconnect.core.Resource
import com.example.realtimeconnect.core.datastore.DataStoreHelper
import com.example.realtimeconnect.features.chat.data.model.ChatListResponseDTO
import com.example.realtimeconnect.features.chat.domain.usecase.FetchChatsUseCase
import com.example.realtimeconnect.features.chat.presentation.chatlist.state.ChatListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val fetchChatsUseCase: FetchChatsUseCase,
    private val dataStoreHelper: DataStoreHelper
) : ViewModel() {
    private val _chatListState: MutableStateFlow<ChatListState> = MutableStateFlow(ChatListState())
    val chatListState = _chatListState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            fetchChatsUseCase.invoke(chatListState.value.page, chatListState.value.perPage)
                .collect {
                    when (it) {
                        is Resource.Error -> handleError(error = it.error)
                        Resource.Loading -> _chatListState.update { state -> state.copy(loading = true) }
                        is Resource.Success -> {
                            Log.d("TAG", "Success ")
                            handleSuccess(it.data)
                        }
                    }
                }
        }
    }

    private fun handleError(error: String) = _chatListState.update {
        it.copy(hasError = true, errorString = error)
    }

    private fun handleSuccess(chatListResponseDto: ChatListResponseDTO) {
        _chatListState.update { state ->
            state.copy(
                loading = false,
                people = state.people.toMutableList().apply {
                    addAll(chatListResponseDto.chatListData?.userIds ?: emptyList())
                },
                groups = state.groups.toMutableList().apply {
                    addAll(chatListResponseDto.chatListData?.groups ?: emptyList())
                }
            )
        }
    }


    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreHelper.setUserAuthKey(authKey = "")
            _chatListState.update {
                it.copy(navigateToLogin = true)
            }
        }
    }

    fun toggleNavigateToLogin() {
        _chatListState.update {
            it.copy(navigateToLogin = false)
        }
    }
}