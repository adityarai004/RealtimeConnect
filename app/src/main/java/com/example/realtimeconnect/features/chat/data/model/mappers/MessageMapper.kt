package com.example.realtimeconnect.features.chat.data.model.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.realtimeconnect.features.chat.data.local.entity.MessageEntity
import com.example.realtimeconnect.features.chat.data.model.DMResponseDTO
import com.example.realtimeconnect.features.chat.data.model.MessageDTO
import java.time.Instant


fun DMResponseDTO.toDomainList(): List<MessageDTO>? {
    return this.messageData?.messages?.mapNotNull {
        it?.let {
            MessageDTO(
                content = it.messageContent,
                senderId = it.senderId,
                receiverId = it.receiverId,
                timestamp = it.createdAt?.substring(11, 16),
                status = it.status
            )
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun DMResponseDTO.toEntityList(): List<MessageEntity>? {
    return this.messageData?.messages?.mapNotNull {
        MessageEntity(
            senderId = it?.senderId ?: "",
            receiverId = it?.receiverId ?: "",
            timestamp = it?.createdAt?.toUnixTimestamp() ?: 0,
            content = it?.messageContent ?: "",
            status = it?.status ?: "",
            remoteId = it?.id,
            recipientStatus = it?.status ?: "sent",
            contentType = it?.contentType ?: ""
        )
    }
}

fun MessageEntity.toDomainModel(): MessageDTO {
    return MessageDTO(
        content = this.content,
        senderId = this.senderId,
        receiverId = this.receiverId,
        timestamp = this.timestamp.toISOString(),
        status = this.status,
        contentType = this.contentType,
    )
}


@RequiresApi(Build.VERSION_CODES.O)
fun String.toUnixTimestamp(): Long {
    return try {
        val instant = Instant.parse(this)
        instant.toEpochMilli()
    } catch (e: Exception) {
        System.currentTimeMillis() // Fallback to current time if parsing fails
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toISOString(): String {
    return try {
        val instant = Instant.ofEpochMilli(this)
        instant.toString() // Returns in ISO 8601 format
    } catch (e: Exception) {
        Instant.now().toString() // Fallback to current time if conversion fails
    }
}
