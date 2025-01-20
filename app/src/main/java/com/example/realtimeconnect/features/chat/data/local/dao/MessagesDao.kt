package com.example.realtimeconnect.features.chat.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.realtimeconnect.features.chat.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessagesDao {
    @Query("SELECT * FROM messages WHERE (senderId = :senderId AND receiverId = :receiverId) OR (senderId = :receiverId AND receiverId = :senderId) ORDER BY timestamp ASC")
    fun getMessages(senderId: String, receiverId: String): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(messages: List<MessageEntity>)

    @Delete
    suspend fun deleteMessage(message: MessageEntity)

    @Query("SELECT COUNT(*) from messages WHERE senderId = :senderId AND receiverId = :receiverId AND status = 'pending'")
    fun getPendingStatus(senderId: String, receiverId: String): Flow<Int>

    @Query("SELECT MAX(timestamp) from messages WHERE (senderId = :senderId AND receiverId = :receiverId) OR (senderId = :receiverId AND receiverId = :senderId)")
    fun getLastMessageTimestamp(senderId: String, receiverId: String): Flow<Long>

    @Query("UPDATE messages SET status = \"delivered\" WHERE (((senderId = :senderId AND receiverId = :receiverId) OR (senderId = :receiverId AND receiverId = :senderId)) AND status = \"sent\") ")
    suspend fun updateMessagesToReceived(senderId: String, receiverId: String)

    @Query("UPDATE messages SET status = \"seen\" WHERE (((senderId = :senderId AND receiverId = :receiverId) OR (senderId = :receiverId AND receiverId = :senderId)) AND status != \"seen\") ")
    suspend fun updateMessagesToSeen(senderId: String, receiverId: String)
}