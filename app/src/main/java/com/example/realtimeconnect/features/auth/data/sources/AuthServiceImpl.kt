package com.example.realtimeconnect.features.auth.data.sources

import com.example.realtimeconnect.features.auth.data.model.AuthResponseDTO
import com.example.realtimeconnect.features.auth.data.model.LoginRequestDTO
import com.example.realtimeconnect.core.constants.NetworkConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(private val client: HttpClient) : AuthService {

    override suspend fun login(loginRequestDTO: LoginRequestDTO): AuthResponseDTO {
        val response = client.post(NetworkConstants.LOGIN) {
            setBody(
                loginRequestDTO
            )
        }.body<AuthResponseDTO>()
        return response;
    }
}