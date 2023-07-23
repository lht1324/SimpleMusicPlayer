package com.overeasy.simplemusicplayer.scenario

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.overeasy.simplemusicplayer.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootActivity : ComponentActivity() {
    private val viewModel: RootViewModel by viewModels()
    private val mediaPlayer by lazy {
        MediaPlayer()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        initUI()
    }

    private fun initUI() {
        setContent {
            val navController = rememberNavController()

            RootNavHost(
                navController = navController,
                mediaPlayer = mediaPlayer
            )
        }
    }
}