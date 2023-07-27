package com.overeasy.simplemusicplayer.scenario

import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.overeasy.simplemusicplayer.R
import com.overeasy.simplemusicplayer.appConfig.MainApplication
import com.overeasy.simplemusicplayer.mediaPlayer.MediaPlayerManager
import com.overeasy.simplemusicplayer.ui.SimpleMusicPlayerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootActivity : ComponentActivity() {
    private val viewModel: RootViewModel by viewModels()

    private lateinit var mediaPlayerManager: MediaPlayerManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        mediaPlayerManager = MediaPlayerManager(
            contentResolver = contentResolver,
            lifecycleScope = lifecycleScope
        )

        initUI()
    }

    override fun onStop() {
        super.onStop()

        mediaPlayerManager.release()
    }

    private fun initUI() {
        setContent {
            val systemUiController = rememberSystemUiController()
            val navController = rememberNavController()

            val isDarkTheme = MainApplication.appPreference.isDarkTheme

            SimpleMusicPlayerTheme(
                darkTheme = isDarkTheme
            ) {
                RootNavHost(
                    navController = navController,
                    mediaPlayerManager = mediaPlayerManager
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