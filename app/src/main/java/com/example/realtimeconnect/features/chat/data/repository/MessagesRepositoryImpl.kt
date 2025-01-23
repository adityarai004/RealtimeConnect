package com.example.realtimeconnect.features.chat.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.realtimeconnect.core.Resource
import com.example.realtimeconnect.core.di.IoDispatcher
import com.example.realtimeconnect.features.chat.data.local.dao.MessagesDao
import com.example.realtimeconnect.features.chat.data.local.entity.MessageEntity
import com.example.realtimeconnect.features.chat.data.model.GroupMessageDTO
import com.example.realtimeconnect.features.chat.data.model.MessageDTO
import com.example.realtimeconnect.features.chat.data.model.mappers.toDomainList
import com.example.realtimeconnect.features.chat.data.model.mappers.toDomainModel
import com.example.realtimeconnect.features.chat.data.model.mappers.toEntityList
import com.example.realtimeconnect.features.chat.data.model.mappers.toISOString
import com.example.realtimeconnect.features.chat.data.model.mappers.toUnixTimestamp
import com.example.realtimeconnect.features.chat.data.source.messages.MessagesDataSource
import com.example.realtimeconnect.features.chat.data.source.socket.SocketDataSource
import com.example.realtimeconnect.features.chat.domain.repository.MessagesRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(
    private val messagesDataSource: MessagesDataSource,
    private val messagesDao: MessagesDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val socket: SocketDataSource
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



    override suspend fun syncRemoteServer(senderId: String, receiverId: String): Flow<Unit> = flow<Unit> {
        try {
            Log.d("Sync", "Starting sync operation")

            val pendingMessages = messagesDao.getPendingStatus(senderId, receiverId).first()
            if (pendingMessages > 0) {
                Log.d("Sync", "Skipping sync - found $pendingMessages pending messages")
                return@flow
            }
            val lastMsgTimeStamp = messagesDao.getLastMessageTimestamp(senderId, receiverId).first()
            val response = messagesDataSource.getMessages(
                receiverId = receiverId,
                timestamp = lastMsgTimeStamp.toISOString()
            )

            if (response.success != true) {
                Log.e("SyncError", "Sync failed: ${response.message}")
                return@flow
            }

            // To update the status of messages which were already there in the local database of the sender
            if (response.messageData?.latestStatus != null) {
                val latestStatus = response.messageData?.latestStatus ?: ""
                if (latestStatus == "seen") {
                    messagesDao.updateMessagesToSeen(senderId, receiverId)
                } else if (latestStatus == "delivered") {
                    messagesDao.updateMessagesToReceived(senderId, receiverId)
                }
            }

            // Fetching and inserting messages into database which are present on server only and not in local database
            val messages = response.toEntityList()
            if (!messages.isNullOrEmpty()) {
                messagesDao.insertMessage(messages)
                Log.d("Sync", "Successfully synced ${messages.size} messages")
            }
        } catch (e: Exception) {
            Log.e("SyncError", "Sync error: ${e.message}")
            throw e // Rethrow to handle in ViewModel if needed
        }
    }.flowOn(ioDispatcher)

    override suspend fun sendMessage(message: String, senderId: String, receiverId: String): Flow<Unit> = flow<Unit> {
        try {
            val rowId = messagesDao.insertMessage(
                listOf(
                    MessageEntity(
                        senderId = senderId,
                        receiverId = receiverId,
                        content = message,
                        timestamp = System.currentTimeMillis()
                    )
                )
            )
            Log.d("SendMessage", "Message inserted with rowId: ${rowId.firstOrNull()}")
            socket.sendMessage(
                mapOf("msg" to message, "receiverId" to receiverId, "senderId" to senderId)
            ) { response ->
                if (response["sent"] == 1) {
                    messagesDao.updateMessageToSent(
                        rowId.firstOrNull() ?: 0,
                        response["msgId"] as? String ?: ""
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("SendMessageError", "Error sending message: ${e.message}")
        }
    }.flowOn(ioDispatcher)

    override suspend fun receiveMessage() {
        try {
            socket.onMessage("receive-message") { data ->
                Log.d("Receive Message", data.toString())
                messagesDao.insertMessage(
                    listOf(
                        MessageEntity(
                            senderId = data["senderId"] as? String ?: "",
                            receiverId = data["receiverId"] as? String ?: "",
                            content = data["msg"] as? String ?: "",
                            timestamp = (data["timestamp"] as String).toUnixTimestamp(),
                            remoteId = data["msgId"] as? String,
                        )
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("SocketError", "Socket Error: ${e.message}")
        }
    }
}