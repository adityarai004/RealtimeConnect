package com.example.realtimeconnect.core.di

import android.util.Log
import com.example.realtimeconnect.core.constants.NetworkConstants
import com.example.realtimeconnect.core.constants.NetworkConstants.BASE_URL
import com.example.realtimeconnect.core.datastore.DataStoreHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val TIMEOUT = 10_000

    @Provides
    fun provideHttpClient(dataStoreHelper: DataStoreHelper): HttpClient {
        return HttpClient(CIO) {
            install(Auth){
                bearer {
                    loadTokens {
                        val token = dataStoreHelper.getUserAuthKey()
                        BearerTokens(token, "")
                    }
                    sendWithoutRequest {
                        it.url.host == "${BASE_URL}/auth/login"
                    }
                }
            }

            install(ContentNegotiation) {
                json(Json {
                    explicitNulls = false
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.v("Logger Ktor=>", message)
                    }
                }
                level = LogLevel.ALL
            }

            install(ResponseObserver) {
                onResponse { response ->
                    Log.d("TAG_HTTP_STATUS_LOGGER", "${response}")
                }
            }


            install(DefaultRequest) {
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                url(NetworkConstants.BASE_URL)
            }
            engine {
                requestTimeout = TIMEOUT.toLong()
            }
        }
    }
}