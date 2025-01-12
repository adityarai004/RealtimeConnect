package com.example.realtimeconnect.features.chat.presentation.groupchat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.realtimeconnect.features.chat.data.model.GroupData
import com.example.realtimeconnect.features.chat.presentation.common.MeMessageBubble
import com.example.realtimeconnect.features.chat.presentation.common.OtherUserMessageBubble
import com.example.realtimeconnect.features.chat.presentation.groupchat.state.GroupChatEvents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupChatScreen(
    groupData: GroupData,
    onBack: () -> Unit,
    groupChatViewModel: GroupChatViewModel = hiltViewModel()
) {
    val groupChatState = groupChatViewModel.groupChatState.collectAsStateWithLifecycle()
    val groupChatMessagesList = groupChatViewModel.messages.collectAsStateWithLifecycle().value
    val usersToAdd = groupChatState.value.users
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        groupChatViewModel.fetchGroupChats(groupId = groupData.id ?: "")
    }

    LaunchedEffect(groupChatState.value.showToast) {
        if(groupChatState.value.showToast){
            groupChatState.value.toastMessage.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
    if (groupChatState.value.isDialogOpen) {
        Dialog(onDismissRequest = {}) {
            BasicAlertDialog(onDismissRequest = {
                groupChatViewModel.handleEvents(
                    GroupChatEvents.OnDismissDialog
                )
            }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column {
                        Text("People to add")
                        Spacer(modifier = Modifier.height(10.dp))
                        LazyColumn {
                            items(usersToAdd.size) { index ->
                                Text(
                                    usersToAdd[index] ?: "",
                                    modifier = Modifier
                                        .clickable {
                                            groupChatViewModel.handleEvents(
                                                GroupChatEvents.OnAddMember(
                                                    groupId = groupData.id ?: "",
                                                    usersToAdd[index] ?: ""
                                                )
                                            )
                                        }
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                actions = {
                    IconButton(onClick = {
                        groupChatViewModel.handleEvents(
                            GroupChatEvents.OnTapAdd(
                                groupData.id ?: ""
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                },
                title = {
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
                    groupChatViewModel.handleEvents(GroupChatEvents.OnTextChange(it))
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
                        groupChatViewModel.handleEvents(GroupChatEvents.OnSend)
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
                                    .padding(horizontal = 20.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color.White,
                                        width = 1.dp
                                    )
                                    .background(Color.Cyan),
                            ) {
                                Text(
                                    message.content ?: "", style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    ),
                                    modifier = Modifier.padding(10.dp)

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