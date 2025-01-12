package com.example.realtimeconnect.features.auth.data.sources

import com.example.realtimeconnect.features.auth.data.model.AuthResponseDTO
import com.example.realtimeconnect.features.auth.data.model.LoginRequestDTO

interface AuthService {
    suspend fun login(loginRequestDTO: LoginRequestDTO): AuthResponseDTO
}