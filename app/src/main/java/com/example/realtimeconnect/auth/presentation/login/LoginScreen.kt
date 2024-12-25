package com.example.realtimeconnect.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.realtimeconnect.R
import com.example.realtimeconnect.auth.presentation.components.AuthTextField
import com.example.realtimeconnect.auth.presentation.components.CustomButton
import com.example.realtimeconnect.auth.presentation.components.PasswordTextField
import com.example.realtimeconnect.auth.presentation.login.components.AccountRow
import com.example.realtimeconnect.auth.presentation.login.state.LoginEvent
import com.example.realtimeconnect.auth.presentation.login.state.LoginState


@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onNavigateToSignUp: () -> Unit,
    onNavigateToHomeScreen: () -> Unit
) {
    val context = LocalContext.current
    val loginState = loginViewModel.uiState.collectAsStateWithLifecycle().value
    val state = rememberScrollState()
    LaunchedEffect(key1 = loginState) {
        if (loginState.isLoginError) {
            Toast.makeText(context, loginState.loginErrorString, Toast.LENGTH_LONG).show()
        }
//        loginViewModel.resetLoginError()
    }
    LaunchedEffect(key1 = loginState.isLoginSuccessful) {
        if (loginState.isLoginSuccessful) {
            onNavigateToHomeScreen()
        }
    }

    Scaffold { innerPadding ->
        MainScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            loginState = loginState, onEmailChange = { inputString ->
                loginViewModel.onUiEvent(
                    uiEvent = LoginEvent.ChangeEmail(
                        inputString
                    )
                )
            },
            onPasswordChange = { inputString ->
                loginViewModel.onUiEvent(LoginEvent.ChangePassword(inputString))
            },
            onSubmit = {
                loginViewModel.onUiEvent(LoginEvent.LoginTap)
            },
            onSignUpClick = onNavigateToSignUp,
            onForgotPasswordClick = {}
        )

    }
    if (loginState.loginInProgress) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .pointerInput(Unit) {}
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }

}

@Composable
private fun MainScreen(
    loginState: LoginState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "Discord\nClone",
            style = TextStyle(
                fontSize = 40.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(70.dp))
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors()
                .copy(containerColor = colorResource(id = R.color.success_green))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                AuthTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = loginState.email,
                    onValueChange = onEmailChange,
                    label = stringResource(id = R.string.enter_your_email),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email,
                        showKeyboardOnFocus = true
                    ),
                    errorText = stringResource(id = loginState.errorState.emailOrMobileErrorState.errorMessageStringResource),
                    isError = loginState.errorState.emailOrMobileErrorState.hasError
                )
                PasswordTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = loginState.password,
                    onValueChange = onPasswordChange,
                    label = stringResource(id = R.string.enter_your_password),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        showKeyboardOnFocus = true
                    ),
                    errorText = stringResource(id = loginState.errorState.passwordErrorState.errorMessageStringResource),
                    isError = loginState.errorState.passwordErrorState.hasError
                )
                CustomButton(
                    onClick = onSubmit,
                    text = stringResource(id = R.string.login),
                    modifier = Modifier.fillMaxWidth()
                )
                AccountRow(
                    modifier = Modifier.fillMaxWidth(),
                    onSignUpClick = onSignUpClick,
                    onForgotPasswordClick = onForgotPasswordClick
                )

            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun LoginScreenPrev() {
    MainScreen(
        onPasswordChange = {},
        onSubmit = {},
        onEmailChange = {},
        loginState = LoginState(),
        onSignUpClick = {},
        onForgotPasswordClick = {}, modifier = Modifier.fillMaxSize(),
    )
}