package com.example.realtimeconnect.chat.presentation.groupchat

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.realtimeconnect.R
import com.example.realtimeconnect.chat.data.model.GroupData
import com.example.realtimeconnect.chat.presentation.chatting.state.ChattingScreenEvents
import com.example.realtimeconnect.chat.presentation.common.MeMessageBubble
import com.example.realtimeconnect.chat.presentation.common.OtherUserMessageBubble
import com.example.realtimeconnect.core.constants.msgCornerSize

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChatScreen(
    groupData: GroupData,
    onBack: () -> Unit,
    groupChatViewModel: GroupChatViewModel = hiltViewModel()
) {
    val groupChatState = groupChatViewModel.groupChatState.collectAsStateWithLifecycle()
    val groupChatMessagesList = groupChatViewModel.messages.collectAsStateWithLifecycle().value
    LaunchedEffect(Unit) {
        groupChatViewModel.fetchGroupChats(groupId = groupData.id ?: "")
    }
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    groupData.name ?: "",
                    style = TextStyle(
                        fontSize = 22.sp,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
            }, modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        bottomBar = {
            TextField(
                groupChatState.value.messageValue,
                {
                    groupChatViewModel.handleEvents(ChattingScreenEvents.OnTextChange(it))
                },
                placeholder = {
                    Text("Type your message here")
                },
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = Color.Black,
                    unfocusedContainerColor = Color.Black,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        groupChatViewModel.handleEvents(ChattingScreenEvents.OnSend)
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                LazyColumn {
                    items(groupChatMessagesList.size) { index ->
                        val message = groupChatMessagesList[index]
                        val isMe = message?.senderId != groupChatState.value.myUserId
                        val alignment =
                            if (isMe) Alignment.CenterEnd else Alignment.CenterStart

                        if (message?.type == "system") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Cyan)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    message.content ?: "", style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )
                                )
                            }
                        } else if (isMe) {
                            MeMessageBubble(
                                alignment = alignment,
                                content = message?.content ?: "",
                                timestamp = message?.createdAt ?: "",
                            )
                        } else {
                            OtherUserMessageBubble(
                                alignment = alignment,
                                content = message?.content ?: "",
                                timestamp = message?.createdAt ?: "",
                                senderId = message?.senderId ?: "XX"
                            )
                        }
                    }
                }
            }
        }
    }
}