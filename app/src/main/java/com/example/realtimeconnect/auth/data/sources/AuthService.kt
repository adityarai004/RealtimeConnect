package com.example.realtimeconnect.auth.data.sources

import com.example.realtimeconnect.auth.data.model.AuthResponseDTO
import com.example.realtimeconnect.auth.data.model.LoginRequestDTO

interface AuthService {
    suspend fun login(loginRequestDTO: LoginRequestDTO): AuthResponseDTO
}