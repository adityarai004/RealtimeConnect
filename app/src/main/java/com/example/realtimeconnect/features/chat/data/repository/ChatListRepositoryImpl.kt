package com.example.realtimeconnect.features.chat.data.repository

import android.util.Log
import com.example.realtimeconnect.core.Resource
import com.example.realtimeconnect.core.di.IoDispatcher
import com.example.realtimeconnect.features.chat.data.model.ChatListResponseDTO
import com.example.realtimeconnect.features.chat.data.model.JoinGroupResponseDTO
import com.example.realtimeconnect.features.chat.data.source.chat.ChatListDataSource
import com.example.realtimeconnect.features.chat.domain.repository.ChatListRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ChatListRepositoryImpl @Inject constructor(
    private val chatListDataSource: ChatListDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ChatListRepository {
    override suspend fun fetchChats(page: Int, perPage: Int): Flow<Resource<ChatListResponseDTO>> =
        flow {
            emit(Resource.Loading)
            try {
                val response = chatListDataSource.fetchChatList(page, perPage)
                if (response.status == false) {
                    emit(
                        Resource.Error(
                            error = response.message ?: "Cannot fetch user at the moment"
                        )
                    )
                } else {
                    emit(Resource.Success(response))
                }
            } catch (e: Exception) {
                Log.d("ChatListError", "Error: $e")
                emit(Resource.Error(error = e.message ?: "Cannot fetch user at the moment"))
            }
        }.flowOn(ioDispatcher)

    override suspend fun fetchNonGroupUsers(
        page: Int,
        perPage: Int,
        groupId: String
    ): Flow<Resource<ChatListResponseDTO>> = flow {
        try {
            val response = chatListDataSource.fetchNonGroupUsers(page, perPage, groupId)
            if (response.status == false) {
                emit(Resource.Error(error = response.message ?: "Cannot fetch user at the moment"))
            } else {
                emit(Resource.Success(response))
            }
        } catch (e: Exception) {
            Log.d("ChatListError", "Error: $e")
            emit(Resource.Error(error = e.message ?: "Cannot fetch user at the moment"))
        }
    }.onStart {
        emit(Resource.Loading)
    }.flowOn(ioDispatcher)

    override suspend fun addUserToGroup(
        groupId: String,
        userId: String
    ): Flow<Resource<JoinGroupResponseDTO>> = flow {
        try {
            val response = chatListDataSource.joinGroup(userId, groupId)
            if (response.status == false) {
                emit(Resource.Error(error = response.message ?: "Cannot fetch user at the moment"))
            } else {
                emit(Resource.Success(response))
            }
        } catch (e: Exception) {
            Log.d("ChatListError", "Error: $e")
            emit(Resource.Error(error = e.message ?: "Cannot fetch user at the moment"))
        }
    }.flowOn(ioDispatcher).onStart { emit(Resource.Loading) }
}