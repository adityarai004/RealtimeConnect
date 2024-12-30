package com.example.realtimeconnect.chat.data.repository

import android.util.Log
import com.example.realtimeconnect.chat.data.model.ChatListResponseDTO
import com.example.realtimeconnect.chat.data.source.chat.ChatListDataSource
import com.example.realtimeconnect.chat.domain.repository.ChatListRepository
import com.example.realtimeconnect.core.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChatListRepositoryImpl @Inject constructor(private val chatListDataSource: ChatListDataSource) :
    ChatListRepository {
    override suspend fun fetchUserIds(page: Int, perPage: Int): Flow<Resource<ChatListResponseDTO>> = flow {
        emit(Resource.Loading)
        try {
            val response = chatListDataSource.fetchChatList(page, perPage)
            if (response.status == false) {
                emit(Resource.Error(error = response.message ?: "Cannot fetch user at the moment"))
            } else {
                emit(Resource.Success(response))
            }
        } catch (e: Exception) {
            Log.d("ChatListError", "Error: $e")
            emit(Resource.Error(error = e.message ?: "Cannot fetch user at the moment"))
        }
    }
}