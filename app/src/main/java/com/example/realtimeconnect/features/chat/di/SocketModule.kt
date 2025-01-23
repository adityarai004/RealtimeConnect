package com.example.realtimeconnect.features.chat.di
import com.example.realtimeconnect.core.constants.NetworkConstants.BASE_URL
import com.example.realtimeconnect.features.chat.data.source.socket.SocketDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SocketModule {

    @Provides
    @Singleton
    fun provideSocket(): Socket = IO.socket(BASE_URL)

    @Provides
    @Singleton
    fun provideSocketDataSource(socket: Socket): SocketDataSource {
        return SocketDataSource(socket)
    }
}