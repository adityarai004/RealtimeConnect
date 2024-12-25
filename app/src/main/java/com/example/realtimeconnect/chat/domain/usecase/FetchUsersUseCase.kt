package com.example.realtimeconnect.chat.domain.usecase

import com.example.realtimeconnect.chat.data.model.ChatListResponseDTO
import com.example.realtimeconnect.chat.domain.repository.ChatListRepository
import com.example.realtimeconnect.core.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchUsersUseCase @Inject constructor(private val chatListRepository: ChatListRepository) {
    suspend operator fun invoke(page: Int, perPage: Int): Flow<Resource<ChatListResponseDTO>> =
        chatListRepository.fetchUserIds(page, perPage)
}