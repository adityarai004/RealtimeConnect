package com.example.realtimeconnect.auth.presentation.components


import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.realtimeconnect.R

@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    errorText: String = "",
    keyboardOptions: KeyboardOptions
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label, fontWeight = FontWeight.Normal, color = colorResource(id = R.color.text_gray))
        },
        maxLines = 1,
        keyboardOptions = keyboardOptions,
        isError = isError,
        supportingText = {
            if (isError) {
                ErrorTextInputField(text = errorText)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(id = R.color.muted_gray),
            unfocusedBorderColor = colorResource(id = R.color.muted_gray)
        )
    )
}

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    errorText: String = "",
    keyboardOptions: KeyboardOptions
) {

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label, fontWeight = FontWeight.Normal, color = colorResource(id = R.color.text_gray))
        },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                val visibleIconText = Pair(
                    first = R.drawable.ic_show_password,
                    second = stringResource(id = R.string.icon_password_visible)
                )
                val hiddenIconText = Pair(
                    first = R.drawable.ic_hide_password,
                    second = stringResource(id = R.string.icon_password_hidden)
                )

                val passwordVisibilityIconAndText =
                    if (passwordVisible) visibleIconText else hiddenIconText

                Icon(
                    painter = painterResource(passwordVisibilityIconAndText.first),
                    contentDescription = passwordVisibilityIconAndText.second,
                    modifier = Modifier.size(20.dp),
                    tint = colorResource(id = R.color.text_gray)
                )
            }
        },
        maxLines = 1,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = keyboardOptions,
        isError = isError,
        supportingText = {
            if (isError) {
                ErrorTextInputField(text = errorText)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(id = R.color.muted_gray),
            unfocusedBorderColor = colorResource(id = R.color.muted_gray)
        )
    )
}

@Composable
fun ErrorTextInputField(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.error
    )
}