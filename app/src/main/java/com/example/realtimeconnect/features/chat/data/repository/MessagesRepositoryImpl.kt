package com.example.realtimeconnect.features.chat.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.realtimeconnect.core.Resource
import com.example.realtimeconnect.core.di.IoDispatcher
import com.example.realtimeconnect.features.chat.data.local.dao.MessagesDao
import com.example.realtimeconnect.features.chat.data.model.GroupMessageDTO
import com.example.realtimeconnect.features.chat.data.model.MessageDTO
import com.example.realtimeconnect.features.chat.data.model.mappers.toDomainList
import com.example.realtimeconnect.features.chat.data.model.mappers.toDomainModel
import com.example.realtimeconnect.features.chat.data.model.mappers.toEntityList
import com.example.realtimeconnect.features.chat.data.model.mappers.toISOString
import com.example.realtimeconnect.features.chat.data.source.messages.MessagesDataSource
import com.example.realtimeconnect.features.chat.domain.repository.MessagesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(
    private val messagesDataSource: MessagesDataSource,
    private val messagesDao: MessagesDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MessagesRepository {
//    override suspend fun getMessages(receiverId: String): Flow<Resource<List<MessageDTO>?>> = flow {
//        emit(Resource.Loading)
//        try {
//            val response = messagesDataSource.getMessages(receiverId)
//            if (response.success == true) {
//                messagesDao.insertMessage(response.toEntityList() ?: emptyList())
//            } else {
//                emit(Resource.Error(response.message ?: "Cannot fetch messages at the moment"))
//            }
//        } catch (e: Exception) {
//            emit(Resource.Error(e.message ?: "Cannot fetch messages at the moment"))
//        }
//    }.flowOn(ioDispatcher)

    override suspend fun getGroupMessages(
        page: Int,
        perPage: Int,
        groupId: String
    ): Flow<Resource<List<GroupMessageDTO?>?>> = flow {
        emit(Resource.Loading)
        try {
            val response = messagesDataSource.getGroupMessages(page, perPage, groupId)
            if (response.status == true) {
                emit(Resource.Success(response.toDomainList()))
            } else {
                emit(
                    Resource.Error(
                        response.message ?: "Something went wrong while fetching messages"
                    )
                )
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Something went wrong while fetching messages"))
        }
    }

    override suspend fun getLocalMessages(
        senderId: String,
        receivedId: String
    ): Flow<List<MessageDTO>> = messagesDao.getMessages(senderId, receivedId).map { item ->
        item.map { it.toDomainModel() }
    }.flowOn(ioDispatcher)


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun syncRemoteServer(senderId: String, receiverId: String) {
        try {
            Log.d("Sync", "Starting sync operation")

            val pendingMessages = messagesDao.getPendingStatus(senderId, receiverId).first()
            if (pendingMessages > 0) {
                Log.d("Sync", "Skipping sync - found $pendingMessages pending messages")
                return
            }

            val lastMsgTimeStamp = messagesDao.getLastMessageTimestamp(senderId, receiverId).first()
            val response = messagesDataSource.getMessages(
                receiverId = receiverId,
                timestamp = lastMsgTimeStamp.toISOString()
            )

            if (response.success == true) {
                val messages = response.toEntityList()
                if (!messages.isNullOrEmpty()) {
                    messagesDao.insertMessage(messages)
                    Log.d("Sync", "Successfully synced ${messages.size} messages")
                }
            } else {
                Log.e("SyncError", "Sync failed: ${response.message}")
            }
        } catch (e: Exception) {
            Log.e("SyncError", "Sync error: ${e.message}")
            throw e // Rethrow to handle in ViewModel if needed
        }
    }
}