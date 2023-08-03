package com.overeasy.simplemusicplayer.scenario

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer.Builder
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.overeasy.simplemusicplayer.appConfig.MainApplication
import com.overeasy.simplemusicplayer.ui.SimpleMusicPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootActivity : ComponentActivity() {
    private val viewModel: RootViewModel by viewModels()

//    private lateinit var mediaPlayerManager: MediaPlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        initUI()
    }

    private fun initUI() {
        setContent {
            val systemUiController = rememberSystemUiController()
            val navController = rememberNavController()

            val isDarkTheme = MainApplication.appPreference.isDarkTheme

            SimpleMusicPlayerTheme(
                darkTheme = false
            ) {
                RootNavHost(
                    navController = navController,
//                    mediaPlayerManager = mediaPlayerManager
                )
            }

            LaunchedEffect(Unit) {
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