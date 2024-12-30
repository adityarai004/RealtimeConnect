package com.example.realtimeconnect.chat.data.model.mappers

import com.example.realtimeconnect.chat.data.model.GroupChatListResponseDTO
import com.example.realtimeconnect.chat.data.model.GroupMessageDTO

fun GroupChatListResponseDTO.toDomainList() : List<GroupMessageDTO?>?{
    return this.data?.mapNotNull {
        GroupMessageDTO(
            type = it?.contentType,
            senderId = it?.senderId,
            content = it?.content,
            id = it?.id,
            createdAt = it?.createdAt?.substring(11,16)
        )
    }
}