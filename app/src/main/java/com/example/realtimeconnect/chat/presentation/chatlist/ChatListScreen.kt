package com.example.realtimeconnect.chat.presentation.chatlist

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ChatListScreen(
    onNavigateToChattingScreen: (String) -> Unit,
    onNavigateToGroupChatScreen: (String) -> Unit,
    chatListViewModel: ChatListViewModel = hiltViewModel()
) {
    val chatListState = chatListViewModel.chatListState.collectAsStateWithLifecycle()
    val chatList = chatListState.value.people
    val groupList = chatListState.value.groups
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text("Personal Chats", Modifier.padding(start = 10.dp))
            LazyColumn {
                items(chatList.size) {
                    ListItem(
                        headlineContent = {
                            Text(chatList[it])
                        },
                        leadingContent = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null)
                        },
                        modifier = Modifier.clickable {
                            onNavigateToChattingScreen(chatList[it])
                            Log.d("TAG","Clicked ${chatList[it]}")
                        }
                    )
                }
            }
            Text("Groups", modifier = Modifier.padding(start = 10.dp))
            LazyColumn {
                items(groupList.size) {
                    ListItem(
                        headlineContent = {
                            Text(groupList[it].name ?: "")
                        },
                        leadingContent = {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null)
                        },
                        modifier = Modifier.clickable {
                            onNavigateToChattingScreen(chatList[it])
                            Log.d("TAG","Clicked ${chatList[it]}")
                        }
                    )
                }
            }
        }

    }
}