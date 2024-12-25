package com.example.realtimeconnect.auth.presentation.login.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.realtimeconnect.R

@Composable
fun AccountRow(modifier: Modifier = Modifier, onSignUpClick: () -> Unit, onForgotPasswordClick: () -> Unit) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(
            onClick = onSignUpClick
        ) {
            Text(
                AnnotatedString(stringResource(id = R.string.sign_up)),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                ),
            )
        }
        TextButton(
            onClick = onForgotPasswordClick
        ) {
            Text(
                AnnotatedString(stringResource(id = R.string.forgot_password)),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                ),
            )
        }
    }
}