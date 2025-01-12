package com.example.realtimeconnect.features.chat.data.repository

import com.example.realtimeconnect.core.Resource
import com.example.realtimeconnect.features.chat.data.model.GroupMessageDTO
import com.example.realtimeconnect.features.chat.data.model.MessageDTO
import com.example.realtimeconnect.features.chat.data.model.mappers.toDomainList
import com.example.realtimeconnect.features.chat.data.source.messages.MessagesDataSource
import com.example.realtimeconnect.features.chat.domain.repository.MessagesRepository
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

    override suspend fun getGroupMessages(page: Int,perPage: Int, groupId: String): Flow<Resource<List<GroupMessageDTO?>?>> = flow {
        emit(Resource.Loading)
        try {
            val response = messagesDataSource.getGroupMessages(page,perPage, groupId)
            if(response.status == true){
                emit(Resource.Success(response.toDomainList()))
            } else {
                emit(Resource.Error(response.message ?: "Something went wrong while fetching messages"))
            }
        } catch (e: Exception){
            emit(Resource.Error(e.message ?: "Something went wrong while fetching messages"))
        }
    }
}