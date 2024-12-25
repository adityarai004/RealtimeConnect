package com.example.realtimeconnect.chat.presentation.chatlist

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ChatListScreen(
    onNavigateToChattingScreen: (String) -> Unit,
    chatListViewModel: ChatListViewModel = hiltViewModel()
) {
    val chatListState = chatListViewModel.chatListState.collectAsStateWithLifecycle()
    val chatList = chatListState.value.people
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
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
        }

    }
}