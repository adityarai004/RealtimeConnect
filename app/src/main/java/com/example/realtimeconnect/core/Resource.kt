package com.example.realtimeconnect.core

sealed class Resource<out T> {
    data class Error(val error: String): Resource<Nothing>()
    data class Success<T>(val data: T): Resource<T>()
    data object Loading: Resource<Nothing>()
}