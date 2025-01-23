package com.example.realtimeconnect.features.chat.domain.usecase

import com.example.realtimeconnect.features.chat.domain.repository.MessagesRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(private val messagesRepository: MessagesRepository) {
    suspend operator fun invoke(message: String, senderId: String, receiverId: String) =
        messagesRepository.sendMessage(message, senderId, receiverId)

}