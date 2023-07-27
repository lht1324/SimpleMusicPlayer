package com.overeasy.simplemusicplayer.scenario.player

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.overeasy.simplemusicplayer.composeComponents.Header
import com.overeasy.simplemusicplayer.mediaPlayer.MediaPlayerManager

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = hiltViewModel(),
    mediaPlayerManager: MediaPlayerManager
) {
    val lazyListState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primary)
    ) {
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            Header(
                title = "헤더",
                doesShowDivider = true
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState
            ) {

            }
        }
        MusicController(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClickPrevious = {
                mediaPlayerManager.moveTo(0)
            },
            onClickPlay = {
                if (mediaPlayerManager.isPlaying)
                    mediaPlayerManager.stop()
                else
                    mediaPlayerManager.start()
            },
            onClickNext = {

            }
        )
    }
}