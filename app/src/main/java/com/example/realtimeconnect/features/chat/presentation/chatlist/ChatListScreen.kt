package com.example.realtimeconnect.features.chat.presentation.chatlist

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    onNavigateToChattingScreen: (String) -> Unit,
    onNavigateToGroupChatScreen: (String,String) -> Unit,
    onNavigateToLoginScreen: () -> Unit,
    chatListViewModel: ChatListViewModel = hiltViewModel()
) {
    val chatListState = chatListViewModel.chatListState.collectAsStateWithLifecycle()
    val chatList = chatListState.value.people
    val groupList = chatListState.value.groups

    LaunchedEffect(chatListState.value.navigateToLogin) {
        if(chatListState.value.navigateToLogin){
            chatListViewModel.toggleNavigateToLogin()
            onNavigateToLoginScreen()
            Log.d("TAG","Navigating to login")
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Chat List")
                },
                actions = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            chatListViewModel.logout()
                        }
                    )
                }
            )
        }
    ) { innerPadding ->
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
                            onNavigateToGroupChatScreen(groupList[it].id ?: "", groupList[it].name ?: "")
                            Log.d("TAG","Clicked ${groupList[it]}")
                        }
                    )
                }
            }
        }

    }
}