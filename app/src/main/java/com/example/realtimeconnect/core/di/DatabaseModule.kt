package com.example.realtimeconnect.core.di

import android.content.Context
import androidx.room.Room
import com.example.realtimeconnect.features.chat.data.local.database.RTCDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideRtcDatabase(
        @ApplicationContext context: Context
    ): RTCDatabase = Room.databaseBuilder(context, RTCDatabase::class.java, "rtc-database").build()

    @Provides
    fun provideMessageDao(rtcDatabase: RTCDatabase) = rtcDatabase.messageDao()
}