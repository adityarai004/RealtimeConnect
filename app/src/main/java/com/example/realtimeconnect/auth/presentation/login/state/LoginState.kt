package com.example.realtimeconnect.auth.presentation.login.state

import androidx.annotation.StringRes
import com.example.realtimeconnect.R


data class LoginState(
    val email: String = "",
    val password: String = "",
    val errorState: LoginErrorState = LoginErrorState(),
    val isLoginSuccessful: Boolean = false,
    val loginInProgress: Boolean = false,
    val isLoginError: Boolean = false,
    val loginErrorString: String = "",
)

data class LoginErrorState(
    val emailOrMobileErrorState: ErrorState = ErrorState(),
    val passwordErrorState: ErrorState = ErrorState()
)

data class ErrorState(
    val hasError: Boolean = false,
    @StringRes val errorMessageStringResource: Int = R.string.empty_string,
    val hasInteracted: Boolean = false
)