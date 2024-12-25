package com.example.realtimeconnect.chat.di

import com.example.realtimeconnect.chat.data.repoistory.ChatListRepositoryImpl
import com.example.realtimeconnect.chat.data.repoistory.MessagesRepositoryImpl
import com.example.realtimeconnect.chat.data.source.ChatListDataSource
import com.example.realtimeconnect.chat.data.source.ChatListDataSourceImpl
import com.example.realtimeconnect.chat.data.source.MessagesDataSource
import com.example.realtimeconnect.chat.data.source.MessagesDataSourceImpl
import com.example.realtimeconnect.chat.domain.repository.ChatListRepository
import com.example.realtimeconnect.chat.domain.repository.MessagesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


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