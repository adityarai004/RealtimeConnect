package com.example.realtimeconnect

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.realtimeconnect.auth.presentation.login.LoginScreen
import com.example.realtimeconnect.chat.data.model.GroupData
import com.example.realtimeconnect.chat.presentation.chatlist.ChatListScreen
import com.example.realtimeconnect.chat.presentation.chatting.ChattingScreen
import com.example.realtimeconnect.chat.presentation.groupchat.GroupChatScreen
import com.example.realtimeconnect.core.constants.ChattingNavigation
import com.example.realtimeconnect.core.constants.GroupChatNavigation
import com.example.realtimeconnect.core.constants.HomeScreenNavigation
import com.example.realtimeconnect.core.constants.LoginNavigation

@Composable
fun DiscordCloneNavigation(navHostController: NavHostController, startDestination: Any) {
    NavHost(navController = navHostController, startDestination = startDestination) {
        composable<LoginNavigation> {
            LoginScreen(onNavigateToSignUp = {}, onNavigateToHomeScreen = {
                navHostController.navigate(HomeScreenNavigation) {
                    popUpTo(LoginNavigation) {
                        inclusive = true
                    }
                }
            })
        }
        composable<HomeScreenNavigation> {
            ChatListScreen(
                onNavigateToChattingScreen = { userId ->
                    navHostController.navigate(
                        ChattingNavigation(userId = userId)
                    )
                },
                onNavigateToGroupChatScreen = { groupId, groupName ->
                    navHostController.navigate(
                        GroupChatNavigation(groupId = groupId, groupName= groupName)
                    )
                },
            )
        }
        composable<ChattingNavigation> { backstackEntry ->
            val chatting: ChattingNavigation = backstackEntry.toRoute<ChattingNavigation>()
            ChattingScreen(userId = chatting.userId, onBack = {
                navHostController.popBackStack()
            })
        }
        composable<GroupChatNavigation> {
            val groupChat: GroupChatNavigation = it.toRoute<GroupChatNavigation>()
            GroupChatScreen(groupData = GroupData(groupChat.groupId, groupChat.groupName), onBack = {
                navHostController.popBackStack()
            })
        }
    }

}