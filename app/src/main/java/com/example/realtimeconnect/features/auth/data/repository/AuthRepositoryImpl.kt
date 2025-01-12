package com.example.realtimeconnect.features.auth.data.repository

import com.example.realtimeconnect.features.auth.data.model.AuthResponseDTO
import com.example.realtimeconnect.features.auth.data.model.LoginRequestDTO
import com.example.realtimeconnect.features.auth.data.sources.AuthService
import com.example.realtimeconnect.features.auth.domain.repository.AuthRepository
import com.example.realtimeconnect.core.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authService: AuthService) :
    AuthRepository {
    override suspend fun login(loginRequestDTO: LoginRequestDTO): Flow<Resource<AuthResponseDTO>> = flow {
        emit(Resource.Loading)
        try{
            val response = authService.login(loginRequestDTO)
            if(response.status == true){
               emit(Resource.Success(response))
            } else {
                emit(Resource.Error(response.message ?: "Something went wrong"))
            }
        } catch (e: Exception){
            emit(Resource.Error(e.message ?: "Something went wrong while logging you in"))
        }
    }
}