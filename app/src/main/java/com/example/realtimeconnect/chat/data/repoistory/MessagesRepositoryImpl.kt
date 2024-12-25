package com.example.realtimeconnect.chat.data.repoistory

import com.example.realtimeconnect.chat.data.model.MessageDTO
import com.example.realtimeconnect.chat.data.model.mappers.toDomainList
import com.example.realtimeconnect.chat.data.source.MessagesDataSource
import com.example.realtimeconnect.chat.domain.repository.MessagesRepository
import com.example.realtimeconnect.core.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(private val messagesDataSource: MessagesDataSource) :
    MessagesRepository {
    override suspend fun getMessages(receiverId: String): Flow<Resource<List<MessageDTO>?>> = flow {
        emit(Resource.Loading)
        try {
            val response = messagesDataSource.getMessages(receiverId)
            if (response.success == true) {
                emit(Resource.Success(response.toDomainList()))
            } else {
                emit(Resource.Error(response.message ?: "Cannot fetch messages at the moment"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Cannot fetch messages at the moment"))
        }
    }
}