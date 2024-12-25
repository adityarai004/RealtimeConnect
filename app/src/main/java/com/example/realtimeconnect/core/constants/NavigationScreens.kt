package com.example.realtimeconnect.core.constants

import kotlinx.serialization.Serializable

@Serializable
object LoginNavigation

@Serializable
object SignUpNavigation

@Serializable
object HomeScreenNavigation

@Serializable
data class ChattingNavigation(val userId: String)