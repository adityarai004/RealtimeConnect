package com.example.realtimeconnect.features.chat.domain.usecase

import com.example.realtimeconnect.features.chat.domain.repository.MessagesRepository
import javax.inject.Inject

class HandleMessageSeenUseCase @Inject constructor(private val messagesRepository: MessagesRepository) {
    suspend operator fun invoke(senderId: String, receiverId: String) = messagesRepository.messageSeen(senderId,receiverId)
}