package com.example.realtimeconnect.features.chat.domain.usecase

import com.example.realtimeconnect.features.chat.domain.repository.MessagesRepository
import javax.inject.Inject

class HandleMessageReceiveUseCase @Inject constructor(private val messagesRepository: MessagesRepository) {
    suspend operator fun invoke() = messagesRepository.receiveMessage()
}