package com.example.realtimeconnect.features.chat.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.realtimeconnect.features.chat.data.local.entity.MediaEntity
import com.example.realtimeconnect.features.chat.data.local.entity.MessageEntity
import com.example.realtimeconnect.features.chat.data.local.entity.MessageWithMedia
import kotlinx.coroutines.flow.Flow

@Dao
interface MessagesDao {

    @Transaction
    @Query("SELECT * FROM messages WHERE (senderId = :senderId AND receiverId = :receiverId) OR (senderId = :receiverId AND receiverId = :senderId) ORDER BY timestamp ASC")
    fun getMessages(senderId: String, receiverId: String): Flow<List<MessageWithMedia>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(messages: List<MessageEntity>): List<Long>

    @Delete
    suspend fun deleteMessage(message: MessageEntity)

    @Query("SELECT COUNT(*) from messages WHERE senderId = :senderId AND receiverId = :receiverId AND status = 'pending'")
    fun getPendingStatus(senderId: String, receiverId: String): Flow<Int>

    @Query("SELECT MAX(timestamp) from messages WHERE (senderId = :senderId AND receiverId = :receiverId) OR (senderId = :receiverId AND receiverId = :senderId)")
    fun getLastMessageTimestamp(senderId: String, receiverId: String): Flow<Long>

    @Query("UPDATE messages SET status = \"delivered\" WHERE (((senderId = :senderId AND receiverId = :receiverId) OR (senderId = :receiverId AND receiverId = :senderId)) AND status = \"sent\") ")
    suspend fun updateMessagesToReceived(senderId: String, receiverId: String)

    @Query("UPDATE messages SET status = \"seen\" WHERE (((senderId = :senderId AND receiverId = :receiverId) OR (senderId = :receiverId AND receiverId = :senderId)) AND status != \"seen\") ")
    fun updateMessagesToSeen(senderId: String, receiverId: String)

    @Query("UPDATE messages SET remoteId= :remoteId, status= \"sent\" WHERE id = :messageId")
    fun updateMessageToSent(messageId: Long, remoteId: String)

    @Query("UPDATE messages SET status= :newStatus WHERE remoteId = :remoteId")
    fun updateMessageStatusVieRemoteId(remoteId: String, newStatus: String)

    @Insert
    fun insertMedia(media: List<MediaEntity>): List<Long>
}