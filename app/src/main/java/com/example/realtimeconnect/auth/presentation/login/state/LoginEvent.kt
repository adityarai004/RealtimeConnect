package com.example.realtimeconnect.auth.presentation.login.state

sealed class LoginEvent{
    data class ChangeEmail(val newValue: String): LoginEvent()
    data class ChangePassword(val newValue: String): LoginEvent()
    data object LoginTap: LoginEvent()
}