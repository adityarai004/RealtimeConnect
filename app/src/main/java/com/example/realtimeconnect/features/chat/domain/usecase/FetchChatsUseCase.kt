package com.example.realtimeconnect.features.chat.domain.usecase

import com.example.realtimeconnect.core.Resource
import com.example.realtimeconnect.features.chat.data.model.ChatListResponseDTO
import com.example.realtimeconnect.features.chat.domain.repository.ChatListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchChatsUseCase @Inject constructor(private val chatListRepository: ChatListRepository) {
    suspend operator fun invoke(page: Int, perPage: Int): Flow<Resource<ChatListResponseDTO>> =
        chatListRepository.fetchChats(page, perPage)
}