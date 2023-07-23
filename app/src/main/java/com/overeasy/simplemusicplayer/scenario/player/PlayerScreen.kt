package com.overeasy.simplemusicplayer.scenario.player

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = hiltViewModel(),
    mediaPlayer: MediaPlayer
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        MusicController(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClickPrevious = {
                mediaPlayer.seekTo(0)
            },
            onClickPlay = {
                if (mediaPlayer.isPlaying)
                    mediaPlayer.stop()
                else
                    mediaPlayer.start()
            },
            onClickNext = {

            }
        )
    }
}