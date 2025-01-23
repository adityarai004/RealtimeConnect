package com.example.realtimeconnect.features.chat.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val senderId: String,
    val receiverId: String,
    val timestamp: Long,
    val remoteId: String? = null,
    val status: String = "pending",
    val contentType: String = "text",
    val content: String,
    val recipientStatus: String = "sent"
)