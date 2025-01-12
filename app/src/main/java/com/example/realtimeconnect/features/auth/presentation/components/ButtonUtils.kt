package com.example.realtimeconnect.features.auth.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.realtimeconnect.R

@Composable
fun CustomButton(modifier: Modifier = Modifier, onClick: () -> Unit, text: String) {
    ElevatedButton(
        onClick = onClick, modifier = modifier, colors = ButtonDefaults.elevatedButtonColors().copy(
            containerColor = colorResource(
                id = R.color.purple_200
            )
        )
    ) {
        Text(
            text,
            fontWeight = FontWeight.Black,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            color = Color.Black
        )
    }
}