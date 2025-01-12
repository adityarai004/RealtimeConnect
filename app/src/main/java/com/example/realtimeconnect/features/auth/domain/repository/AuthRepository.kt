package com.example.realtimeconnect.features.auth.domain.repository

import com.example.realtimeconnect.features.auth.data.model.AuthResponseDTO
import com.example.realtimeconnect.features.auth.data.model.LoginRequestDTO
import com.example.realtimeconnect.core.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository{
    suspend fun login(loginRequestDTO: LoginRequestDTO): Flow<Resource<AuthResponseDTO>>
}
