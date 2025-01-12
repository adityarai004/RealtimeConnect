package com.example.realtimeconnect.features.chat.domain.usecase

import com.example.realtimeconnect.features.chat.domain.repository.ChatListRepository
import javax.inject.Inject

class JoinGroupUseCase @Inject constructor(private val chatRepository: ChatListRepository) {
    suspend operator fun invoke(groupId: String, userId: String) =
        chatRepository.addUserToGroup(groupId, userId)
}