package com.example.realtimeconnect.features.chat.data.model.mappers

import com.example.realtimeconnect.features.chat.data.model.DMResponseDTO
import com.example.realtimeconnect.features.chat.data.model.MessageDTO


fun DMResponseDTO.toDomainList(): List<MessageDTO>? {
    return this.messageData?.messages?.mapNotNull {
        it?.let {
            MessageDTO(
                content = it.messageContent,
                senderId = it.senderId,
                receiverId = it.receiverId,
                timestamp = it.createdAt?.substring(11,16),
                status = it.status
            )
        }

    }
}