package com.example.realtimeconnect.chat.domain.usecase

import com.example.realtimeconnect.chat.data.model.MessageDTO
import com.example.realtimeconnect.chat.domain.repository.MessagesRepository
import com.example.realtimeconnect.core.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(private val messagesRepository: MessagesRepository) {
    suspend operator fun invoke(receiverId: String): Flow<Resource<List<MessageDTO>?>> =
        messagesRepository.getMessages(receiverId)

}