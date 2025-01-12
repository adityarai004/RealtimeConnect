package com.example.realtimeconnect.features.chat.domain.usecase

import com.example.realtimeconnect.features.chat.domain.repository.MessagesRepository
import javax.inject.Inject

class GetGroupMessagesUseCase @Inject constructor(private val messagesRepository: MessagesRepository) {
    suspend operator fun invoke(page: Int, perPage: Int, groupId: String) =
        messagesRepository.getGroupMessages(page, perPage, groupId)
}