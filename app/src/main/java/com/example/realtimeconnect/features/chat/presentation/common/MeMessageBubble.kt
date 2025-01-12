package com.example.realtimeconnect.features.chat.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realtimeconnect.R
import com.example.realtimeconnect.core.constants.msgCornerSize

@Composable
fun MeMessageBubble(modifier: Modifier = Modifier, alignment: Alignment, content: String, timestamp: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        contentAlignment = alignment
    ) {
        Column(
            Modifier
                .background(colorResource(R.color.sent_message_bg), shape = RoundedCornerShape(
                    topStartPercent = msgCornerSize,
                    bottomEndPercent = msgCornerSize,
                    bottomStartPercent = msgCornerSize
                )
                )
                .border(
                    border = BorderStroke(1.dp, Color.White),
                    shape = RoundedCornerShape(
                        topStartPercent = msgCornerSize,
                        bottomEndPercent = msgCornerSize,
                        bottomStartPercent = msgCornerSize
                    )
                )
                .padding(15.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                content,
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(Modifier.height(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 5.dp)
            ) {
                Text(
                    timestamp,
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Light,
                        fontSize = 8.sp
                    ),
                )
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .size(15.dp),
                    tint = Color.Red,
                )
            }
        }
    }
}