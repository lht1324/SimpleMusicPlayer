package com.overeasy.simplemusicplayer.scenario.main

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
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

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
                MainNavHost(
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