package com.overeasy.simplemusicplayer.scenario.main.player

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.overeasy.simplemusicplayer.composeComponents.Header
import com.overeasy.simplemusicplayer.composeComponents.dpToSp
import com.overeasy.simplemusicplayer.composeComponents.noRippleClickable
import com.overeasy.simplemusicplayer.room.entity.MusicData
import com.overeasy.simplemusicplayer.ui.fontFamily

@Composable
fun PlayerScreen(
    viewModel: PlayerViewModel = hiltViewModel(),
    onClickSetting: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val density = LocalDensity.current

    val lazyListState = rememberLazyListState()

    var controllerHeight by remember { mutableStateOf(0) }

    val isPlaying by remember { viewModel.isPlayingState }
    val progress by viewModel.progressFlow.collectAsState(initial = 0f)
    val loopType by viewModel.loopType.collectAsState()
    val musicDataList = remember { viewModel.musicDataList }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primary)
    ) {
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            Header(
                doesShowDivider = true,
                endContent = {
                    Text(
                        text = "설정",
                        modifier = Modifier.noRippleClickable(onClick = onClickSetting),
                        color = MaterialTheme.colors.secondary,
                        fontSize = 24.dpToSp(),
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily
                    )
                }
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState
            ) {
                itemsIndexed(musicDataList) { index, musicData ->
                    MusicDataItem(
                        musicData = musicData,
                        onClick = {
                            viewModel.onClickItem(isPlaying, index)
                        }
                    )
                    if (index != musicDataList.size - 1) {
                        Divider(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colors.secondary,
                            thickness = 2.dp
                        )
                    } else {
                        Spacer(modifier = Modifier.height(density.run { controllerHeight.toDp() }))
                    }
                }
            }
        }
        MusicController(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .onSizeChanged { size ->
                    if (controllerHeight != size.height)
                        controllerHeight = size.height
                },
            isPlaying = isPlaying,
            progress = progress,
            loopType = loopType,
            onClickRepeat = {
                viewModel.onClickRepeat(loopType)
            },
            onClickPrevious = viewModel::onClickPrevious,
            onClickPlay = {
                viewModel.onClickPlay(isPlaying)
            },
            onClickNext = viewModel::onClickNext,
            onProgressBarDragged = viewModel::onProgressBarDragged
        )
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> viewModel.prepare()
                Lifecycle.Event.ON_DESTROY -> viewModel.release()
                else -> { /* no-op */ }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
private fun MusicDataItem(
    modifier: Modifier = Modifier,
    musicData: MusicData,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Text(
            text = musicData.name,
            modifier = Modifier.padding(
                vertical = 16.dp,
                horizontal = 24.dp
            ),
            color = MaterialTheme.colors.secondary,
            fontSize = 24.dpToSp(),
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamily
        )
    }
}