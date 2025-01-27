package com.example.realtimeconnect.features.chat.data.model.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.realtimeconnect.features.chat.data.local.entity.MessageEntity
import com.example.realtimeconnect.features.chat.data.local.entity.MessageWithMedia
import com.example.realtimeconnect.features.chat.data.model.DMResponseDTO
import com.example.realtimeconnect.features.chat.data.model.MessageDTO
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale
import java.util.TimeZone


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

fun DMResponseDTO.toEntityList(): List<MessageEntity>? {
    return this.messageData?.messages?.mapNotNull {
        MessageEntity(
            senderId = it?.senderId ?: "",
            receiverId = it?.receiverId ?: "",
            timestamp = it?.createdAt?.toUnixTimestamp() ?: 0,
            content = it?.messageContent ?: "",
            status = it?.status ?: "",
            remoteId = it?.id,
            contentType = it?.contentType ?: ""
        )
    }
}

fun MessageWithMedia.toDomainModel(): MessageDTO {
    return MessageDTO(
        content = this.message.content,
        senderId = this.message.senderId,
        receiverId = this.message.receiverId,
        timestamp = this.message.timestamp.toISOString(),
        status = this.message.status,
        contentType = this.message.contentType,
        fileUri = this.mediaEntity?.fileUri,
        remoteUrl = this.mediaEntity?.remoteUrl,
        mediaType = this.mediaEntity?.mediaType ?: "image"
    )
}

fun String.toUnixTimestamp(): Long {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Use Instant for API level O and above
            val instant = Instant.parse(this)
            instant.toEpochMilli()
        } else {
            // Use SimpleDateFormat for below API level O
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            sdf.parse(this)?.time ?: System.currentTimeMillis()
        }
    } catch (e: Exception) {
        System.currentTimeMillis() // Fallback to current time if parsing fails
    }
}
fun Long.toISOString(): String {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Use Instant for API level O and above
            val instant = Instant.ofEpochMilli(this)
            instant.toString() // Returns in ISO 8601 format
        } else {
            // Use SimpleDateFormat for below API level O
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            sdf.format(Date(this)) // Convert Long to Date and format it
        }
    } catch (e: Exception) {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date()) // Fallback to current time if conversion fails
    }
}
