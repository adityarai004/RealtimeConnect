package com.example.realtimeconnect.features.auth.domain.usecase

import com.example.realtimeconnect.features.auth.data.model.AuthResponseDTO
import com.example.realtimeconnect.features.auth.data.model.LoginRequestDTO
import com.example.realtimeconnect.features.auth.domain.repository.AuthRepository
import com.example.realtimeconnect.core.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): Flow<Resource<AuthResponseDTO>> {
        return authRepository.login(LoginRequestDTO(username = username, password = password))
    }
}