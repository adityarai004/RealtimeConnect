package com.example.realtimeconnect.features.chat.domain.usecase

import com.example.realtimeconnect.features.chat.domain.repository.ChatListRepository
import javax.inject.Inject

class FetchNonGroupUsersUseCase @Inject constructor(private val chatListRepository: ChatListRepository) {

    suspend operator fun invoke(page: Int = 1, perPage: Int = 10, groupId: String) = chatListRepository.fetchNonGroupUsers(page, perPage, groupId)
}