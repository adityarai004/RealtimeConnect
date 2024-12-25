package com.example.realtimeconnect.auth.di

import com.example.realtimeconnect.auth.data.repository.AuthRepositoryImpl
import com.example.realtimeconnect.auth.data.sources.AuthService
import com.example.realtimeconnect.auth.data.sources.AuthServiceImpl
import com.example.realtimeconnect.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    abstract fun bindAuthService(authServiceImpl: AuthServiceImpl): AuthService

    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository
}