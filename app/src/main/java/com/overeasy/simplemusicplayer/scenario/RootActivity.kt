package com.overeasy.simplemusicplayer.scenario

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.overeasy.simplemusicplayer.ui.SimpleMusicPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootActivity : ComponentActivity() {
    private val viewModel: RootViewModel by viewModels()

    override fun onStart() {
        super.onStart()

        initUI()
    }

    private fun initUI() {
        setContent {
            val systemUiController = rememberSystemUiController()
            val navController = rememberNavController()

            val isDarkTheme by viewModel.isDarkTheme.collectAsState(initial = false)

            SimpleMusicPlayerTheme(
                darkTheme = isDarkTheme
            ) {
                RootNavHost(
                    navController = navController
                )
            }

            LaunchedEffect(isDarkTheme) {
                systemUiController.setStatusBarColor(
                    if (isDarkTheme)
                        Color.Black
                    else
                        Color.White
                )
            }
        }
    }
}