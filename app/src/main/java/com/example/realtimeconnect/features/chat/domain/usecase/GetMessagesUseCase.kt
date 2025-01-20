package com.example.realtimeconnect.features.chat.domain.usecase

import com.example.realtimeconnect.features.chat.data.model.MessageDTO
import com.example.realtimeconnect.features.chat.domain.repository.MessagesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(private val messagesRepository: MessagesRepository) {
    suspend operator fun invoke(senderId: String, receiverId: String): Flow<List<MessageDTO>> =
        messagesRepository.getLocalMessages(senderId, receiverId)
}