package com.example.realtimeconnect.features.chat.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "messages", indices = [Index(value = ["remoteId"], unique = true)])
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val senderId: String,
    val receiverId: String,
    val timestamp: Long,
    val remoteId: String? = null,
    val status: String = "pending",
    val contentType: String = "text",
    val content: String,
    val fileUri: String? = null,
    val recipientStatus: String = "sent"
)

@Entity(tableName = "media", indices = [Index(value = ["remoteUrl"], unique = true)])
data class MediaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val messageId: Long = 0L,
    val mediaType: String = "image",
    val fileUri: String? = null,
    val remoteUrl: String? = null
)

data class MessageWithMedia(
    @Embedded val message: MessageEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "messageId"
    )
    val mediaEntity: MediaEntity? = null
)