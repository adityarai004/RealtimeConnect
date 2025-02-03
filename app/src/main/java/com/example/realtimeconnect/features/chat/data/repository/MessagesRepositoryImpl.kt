package com.example.realtimeconnect.features.chat.data.repository

import android.util.Log
import com.example.realtimeconnect.core.Resource
import com.example.realtimeconnect.core.di.IoDispatcher
import com.example.realtimeconnect.features.chat.data.local.dao.MessagesDao
import com.example.realtimeconnect.features.chat.data.local.entity.MediaEntity
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
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(
    private val messagesDataSource: MessagesDataSource,
    private val messagesDao: MessagesDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val socket: SocketDataSource
) : MessagesRepository {

    companion object {
        private const val TAG = "MessagesRepositoryImpl"
    }

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

    override suspend fun syncRemoteServer(senderId: String, receiverId: String): Flow<Unit> =
        flow<Unit> {
            try {
                Log.d("Sync", "Starting sync operation")

                val pendingMessages = messagesDao.getPendingStatus(senderId, receiverId).first()
                if (pendingMessages > 0) {
                    Log.d("Sync", "Skipping sync - found $pendingMessages pending messages")
                    return@flow
                }
                val lastMsgTimeStamp =
                    messagesDao.getLastMessageTimestamp(senderId, receiverId).first()
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

    override suspend fun sendMessage(message: String, senderId: String, receiverId: String):
            Flow<Unit> = flow<Unit> {
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

    override suspend fun messageSeen(senderId: String, receiverId: String) {
        try {
            Log.d("MessageSeen", "Sending message seen event")
            socket.onMessage("messages-seen") { data ->
                Log.d("Message Seen", data.toString())
                messagesDao.updateMessagesToSeen(senderId, receiverId)
            }
        } catch (e: Exception) {
            Log.e("SocketError", "Socket Error: ${e.message}")
        }
    }

    override suspend fun messageStatusUpdate() {
        try {
            socket.onMessage("message-status") { data ->
                Log.d("Message Status", data.toString())
                val msgId = data["id"] as String
                val status = data["status"] as String
                messagesDao.updateMessageStatusVieRemoteId(msgId, status)
            }
        } catch (e: Exception) {
            Log.e("SocketError", "Socket Error: ${e.message}")
        }
    }

    override suspend fun insertMediaMessageToLocal(
        senderId: String,
        receiverId: String,
        content: String,
        file: File
    ): Pair<Long?, Long?> {
        return try {
            withContext(ioDispatcher) {
                val rowId = messagesDao.insertMessage(
                    listOf(
                        MessageEntity(
                            senderId = senderId,
                            receiverId = receiverId,
                            content = content,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                )

                Log.d(TAG, "sendFile: ${rowId.firstOrNull()}")

                val mediaRowId = messagesDao.insertMedia(
                    listOf(
                        MediaEntity(
                            messageId = rowId.firstOrNull() ?: 0,
                            mediaType = "image",
                            fileUri = file.path,
                        )
                    )
                )

                Log.d(TAG, "sendFile: ${mediaRowId.firstOrNull()}")
                Pair(rowId.firstOrNull(), mediaRowId.firstOrNull())
            }
        } catch (e: Exception) {
            Log.e("SendFileError", "Error sending file: ${e.message}")
            Pair(null, null)
        }
    }

    override suspend fun sendMediaMessageAndUpdateLocal(
        receiverId: String,
        content: String,
        filePath: String,
        fileName: String,
        mimeType: String,
        messageRowId: Long,
        mediaRowId: Long
    ): Boolean {
        return try {
            withContext(ioDispatcher) {
                val response = messagesDataSource.uploadMedia(receiverId, content, filePath, fileName, mimeType)
                if (response.status == true) {
                    messagesDao.updateMessageToSent(
                        messageRowId,
                        response.uploadMediaData?.message?.id ?: ""
                    )
                    messagesDao.updateMediaRemoteUrl(
                        mediaRowId,
                        response.uploadMediaData?.media?.url ?: ""
                    )
                    true
                } else {
                    false
                }
            }
        } catch (e: Exception) {
            Log.e("SendFileError", "Error sending file: ${e.message}")
            false
        }
    }


}