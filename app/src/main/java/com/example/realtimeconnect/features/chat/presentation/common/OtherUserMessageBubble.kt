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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realtimeconnect.R
import com.example.realtimeconnect.core.constants.msgCornerSize

@Composable
fun OtherUserMessageBubble(modifier: Modifier = Modifier,alignment: Alignment, content: String, timestamp: String, senderId: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        contentAlignment = alignment
    ) {
        Row {
            Text(senderId.substring(0,1), modifier = Modifier.clip(CircleShape).background(Color.Red))
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                Modifier
                    .background(
                        colorResource(R.color.received_message_bg),
                        shape = RoundedCornerShape(
                            topEndPercent = msgCornerSize,
                            bottomStartPercent = msgCornerSize,
                            bottomEndPercent = msgCornerSize
                        )
                    )
                    .border(
                        border = BorderStroke(1.dp, Color.White),
                        shape = RoundedCornerShape(
                            topEndPercent = msgCornerSize,
                            bottomStartPercent = msgCornerSize,
                            bottomEndPercent = msgCornerSize
                        )
                    )
                    .padding(15.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    content,
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    timestamp,
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Light,
                        fontSize = 8.sp
                    ),
                )
            }
        }
    }
}