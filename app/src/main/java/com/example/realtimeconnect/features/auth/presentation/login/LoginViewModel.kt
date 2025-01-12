package com.example.realtimeconnect.features.auth.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimeconnect.features.auth.domain.usecase.LoginUseCase
import com.example.realtimeconnect.features.auth.presentation.login.state.LoginEvent
import com.example.realtimeconnect.features.auth.presentation.login.state.LoginState
import com.example.realtimeconnect.core.Resource
import com.example.realtimeconnect.core.constants.SharedPrefsConstants
import com.example.realtimeconnect.core.datastore.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase, private val dataStoreHelper: DataStoreHelper): ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _loginState.asStateFlow();
    fun onUiEvent(uiEvent: LoginEvent) {
        when (uiEvent) {
            is LoginEvent.ChangeEmail -> {
                _loginState.update { state ->
                    state.copy(email = uiEvent.newValue)
                }
            }
            is LoginEvent.ChangePassword -> {
                _loginState.update { state ->
                    state.copy(password = uiEvent.newValue)
                }
            }
            LoginEvent.LoginTap -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _loginState.update { it.copy(loginInProgress = true, isLoginError = false) }

                    loginUseCase(uiState.value.email, uiState.value.password).collect { resource ->
                        when (resource) {
                            is Resource.Error -> {
                                _loginState.update {
                                    it.copy(
                                        loginInProgress = false,
                                        isLoginError = true,
                                        loginErrorString = resource.error
                                    )
                                }
                            }
                            Resource.Loading -> {
                                _loginState.update { it.copy(loginInProgress = true) }
                            }
                            is Resource.Success -> {
                                _loginState.update {
                                    dataStoreHelper.setUserAuthKey(resource.data.data?.authToken ?: "")
                                    dataStoreHelper.storeString(SharedPrefsConstants.USERID, resource.data.data?.userId ?: "")
                                    Log.i("Login", "Logged In successfully")
                                    it.copy(
                                        loginInProgress = false,
                                        isLoginSuccessful = true,
                                        isLoginError = false,
                                        loginErrorString = ""
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}