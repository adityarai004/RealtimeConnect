package com.example.realtimeconnect.auth.domain.repository

import com.example.realtimeconnect.auth.data.model.AuthResponseDTO
import com.example.realtimeconnect.auth.data.model.LoginRequestDTO
import com.example.realtimeconnect.core.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository{
    suspend fun login(loginRequestDTO: LoginRequestDTO): Flow<Resource<AuthResponseDTO>>
}
