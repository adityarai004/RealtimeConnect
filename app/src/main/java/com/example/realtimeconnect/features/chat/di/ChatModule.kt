package com.example.realtimeconnect.features.chat.di

import com.example.realtimeconnect.features.chat.data.repository.ChatListRepositoryImpl
import com.example.realtimeconnect.features.chat.data.repository.MessagesRepositoryImpl
import com.example.realtimeconnect.features.chat.data.source.chat.ChatListDataSource
import com.example.realtimeconnect.features.chat.data.source.chat.ChatListDataSourceImpl
import com.example.realtimeconnect.features.chat.data.source.messages.MessagesDataSource
import com.example.realtimeconnect.features.chat.data.source.messages.MessagesDataSourceImpl
import com.example.realtimeconnect.features.chat.domain.repository.ChatListRepository
import com.example.realtimeconnect.features.chat.domain.repository.MessagesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class ChatModule {

    @Binds
    abstract fun bindChatListDataSource(chatListDataSourceImpl: ChatListDataSourceImpl): ChatListDataSource

    @Binds
    abstract fun bindChatListRepository(chatListRepositoryImpl: ChatListRepositoryImpl): ChatListRepository

    @Binds
    abstract fun bindMessageDataSource(messagesDataSourceImpl: MessagesDataSourceImpl): MessagesDataSource

    @Binds
    abstract fun bindMessagesRepository(messagesRepositoryImpl: MessagesRepositoryImpl): MessagesRepository

}