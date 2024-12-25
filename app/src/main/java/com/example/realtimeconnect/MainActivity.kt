package com.example.realtimeconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.realtimeconnect.core.constants.HomeScreenNavigation
import com.example.realtimeconnect.core.constants.LoginNavigation
import com.example.realtimeconnect.ui.theme.DiscordCloneTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            mainViewModel.isLoading.value
        }
        setContent {
            val navController = rememberNavController()
            val isLoggedIn = mainViewModel.isLoggedIn.collectAsState()
            val isLoading = mainViewModel.isLoading.collectAsState()
            DiscordCloneTheme {
                if (!isLoading.value) {
                    val startDestination: Any =
                        if (isLoggedIn.value) HomeScreenNavigation else LoginNavigation
                    Box(Modifier.statusBarsPadding()) {
                        Box(Modifier.navigationBarsPadding()) {
                            DiscordCloneNavigation(navController, startDestination = startDestination)
                        }
                    }

                }
            }

        }
    }
}