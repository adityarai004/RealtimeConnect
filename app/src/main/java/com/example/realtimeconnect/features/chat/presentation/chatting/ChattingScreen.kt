package com.example.realtimeconnect.features.chat.presentation.chatting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.realtimeconnect.R
import com.example.realtimeconnect.core.constants.msgCornerSize
import com.example.realtimeconnect.features.chat.presentation.chatting.state.ChattingScreenEvents
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChattingScreen(
    userId: String,
    chattingViewModel: ChattingViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val chattingState = chattingViewModel.chattingState.collectAsStateWithLifecycle()
    val messageList = chattingViewModel.messageState.collectAsStateWithLifecycle().value
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(messageList.size, key2 = chattingState.value.otherGuyTyping) {
        if (chattingState.value.otherGuyTyping) {
            coroutineScope.launch {
                lazyListState.scrollToItem(messageList.lastIndex + 1)
            }
        } else if (messageList.isNotEmpty()) {
            coroutineScope.launch {
                lazyListState.animateScrollToItem(messageList.lastIndex)
            }
        }
    }
    LaunchedEffect(Unit) {
        chattingViewModel.connect(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    userId,
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
                chattingState.value.messageValue,
                {
                    chattingViewModel.handleEvents(ChattingScreenEvents.OnTextChange(it))
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
                        chattingViewModel.handleEvents(ChattingScreenEvents.OnSend)
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                LazyColumn(
                    modifier = Modifier
                        .consumeWindowInsets(innerPadding)
                        .padding(bottom = 60.dp),
                    state = lazyListState
                ) {
                    items(messageList.size) { index ->
                        val message = messageList[index]
                        val isMe = message.receiverId == userId
                        val alignment =
                            if (isMe) Alignment.CenterEnd else Alignment.CenterStart

                        val bgColor =
                            if (isMe) colorResource(R.color.sent_message_bg)
                            else colorResource(R.color.received_message_bg)
                        val textColor =
                            if (isMe) Color.White else Color.Black
                        val cornerShape = if (isMe) RoundedCornerShape(
                            topStartPercent = msgCornerSize,
                            bottomEndPercent = msgCornerSize,
                            bottomStartPercent = msgCornerSize
                        ) else RoundedCornerShape(
                            topEndPercent = msgCornerSize,
                            bottomStartPercent = msgCornerSize,
                            bottomEndPercent = msgCornerSize
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            contentAlignment = alignment
                        ) {
                            Column(
                                Modifier
                                    .background(bgColor, shape = cornerShape)
                                    .border(
                                        border = BorderStroke(1.dp, Color.White),
                                        shape = cornerShape
                                    )
                                    .padding(15.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    message.content ?: "",
                                    style = TextStyle(
                                        color = textColor,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(Modifier.height(2.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(end = 5.dp)
                                ) {
                                    Text(
                                        message.timestamp ?: "",
                                        style = TextStyle(
                                            color = textColor,
                                            fontWeight = FontWeight.Light,
                                            fontSize = 8.sp
                                        ),
                                    )
                                    if (isMe) {
                                        if (message.status == "sent") {
                                            Icon(Icons.Default.Done, contentDescription = null)
                                        } else if (message.status == "delivered") {
                                            Icon(
                                                Icons.AutoMirrored.Filled.Send,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .padding(start = 5.dp)
                                                    .size(15.dp),
                                                tint = Color.Red,
                                            )
                                        } else if (message.status == "seen") {
                                            Icon(
                                                Icons.Default.Face,
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .padding(start = 5.dp)
                                                    .size(15.dp),
                                                tint = Color.Blue,
                                            )
                                        }
                                    }
                                }

                            }
                        }
                    }
                    item {
                        val composition by rememberLottieComposition(LottieCompositionSpec.Url("https://lottie.host/d38454c0-b6d3-4822-b31d-3a95096fd9c4/sOLaRj7tXR.lottie"))
                        val progress by animateLottieCompositionAsState(
                            composition,
                            iterations = LottieConstants.IterateForever
                        )
                        if (chattingState.value.otherGuyTyping) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 3.dp)
                            ) {
                                LottieAnimation(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .align(Alignment.CenterStart),
                                    composition = composition,
                                    progress = { progress },
                                )
                            }
                        }


                    }
                }
            }
        }
    }
}