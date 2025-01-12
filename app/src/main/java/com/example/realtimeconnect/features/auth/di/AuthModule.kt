package com.example.realtimeconnect.features.auth.di

import com.example.realtimeconnect.features.auth.data.repository.AuthRepositoryImpl
import com.example.realtimeconnect.features.auth.data.sources.AuthService
import com.example.realtimeconnect.features.auth.data.sources.AuthServiceImpl
import com.example.realtimeconnect.features.auth.domain.repository.AuthRepository
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