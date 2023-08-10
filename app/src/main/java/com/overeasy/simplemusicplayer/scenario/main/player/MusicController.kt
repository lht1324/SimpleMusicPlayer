package com.overeasy.simplemusicplayer.scenario.main.player

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.overeasy.simplemusicplayer.composeComponents.dpToSp
import com.overeasy.simplemusicplayer.composeComponents.noRippleClickable
import com.overeasy.simplemusicplayer.model.LoopType
import com.overeasy.simplemusicplayer.ui.fontFamily
import kotlinx.coroutines.delay

@Composable
fun MusicController(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    progress: Float,
    loopType: LoopType,
    onClickRepeat: () -> Unit,
    onClickPrevious: () -> Unit,
    onClickPlay: () -> Unit,
    onClickNext: () -> Unit,
    onProgressBarDragged: (Float) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.primary)
            .noRippleClickable { /* no-op */ },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.secondary,
            thickness = 1.5.dp
        )
        Spacer(modifier = Modifier.height(20.dp))
        ProgressBar(
            progress = progress,
            onProgressBarDragged = onProgressBarDragged
        )
        Spacer(modifier = Modifier.height(20.dp))
        Buttons(
            isPlaying = isPlaying,
            loopType = loopType,
            onClickRepeat = onClickRepeat,
            onClickPrevious = onClickPrevious,
            onClickPlay = onClickPlay,
            onClickNext = onClickNext
        )
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun ProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    onProgressBarDragged: (Float) -> Unit
) {
    val density = LocalDensity.current

    val parentHorizontalPadding = 48
    val progressButtonWidth = 10
    val progressBarWidth = LocalConfiguration.current.screenWidthDp - (parentHorizontalPadding + progressButtonWidth)
    var delta by remember { mutableStateOf(0.0f) }
    var isDragging by remember { mutableStateOf(false )}
    var currentProgress by remember { mutableStateOf(0f) }
    val buttonOffsetX by remember {
        derivedStateOf {
            (currentProgress * (progressBarWidth).toFloat()).dp
        }
    }

    Box(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val offsetX = density.run { offset.x.toDp() }.value
                    onProgressBarDragged(offsetX / progressBarWidth.toFloat())
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(color = MaterialTheme.colors.secondary)
                .align(Alignment.Center)
        )
        Box(
            modifier = Modifier
                .padding(1.dp)
                .fillMaxWidth(fraction = currentProgress)
                .height(8.dp)
                .background(color = MaterialTheme.colors.primary)
                .align(Alignment.CenterStart)
        )
        Box(
            modifier = Modifier
                .offset(x = buttonOffsetX)
                .width(10.dp)
                .height(14.dp)
                .background(color = MaterialTheme.colors.primary)
                .border(
                    color = MaterialTheme.colors.primaryVariant,
                    width = 1.dp
                )
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            isDragging = true
                        },
                        onDragEnd = {
                            isDragging = false
                            delta = 0f
                        },
                        onDrag = { _, dragAmount ->
                            delta = dragAmount.x
                        },
                    )
                }
        )

        LaunchedEffect(progress, delta) {
            currentProgress = if (isDragging) {
                (buttonOffsetX + density.run { delta.toDp() }).value / progressBarWidth.toFloat()
            } else {
                progress
            }
        }

        LaunchedEffect(buttonOffsetX) {
            if (isDragging && buttonOffsetX.value / progressBarWidth.toFloat() in 0.0f..1.0f) {
                onProgressBarDragged(buttonOffsetX.value / progressBarWidth.toFloat())
            }
        }
    }
}

@Composable
private fun Buttons(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    loopType: LoopType,
    onClickRepeat: () -> Unit,
    onClickPrevious: () -> Unit,
    onClickPlay: () -> Unit,
    onClickNext: () -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp)
    ) {
        RepeatButton(
            modifier = Modifier.align(Alignment.End),
            loopType = loopType,
            onClick = onClickRepeat
        )
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ProgressButton(
                isNext = true,
                onClick = onClickPrevious
            )
            PlayButton(
                isPlaying = isPlaying,
                onClick = onClickPlay
            )
            ProgressButton(
                isNext = false,
                onClick = onClickNext
            )
        }
    }
}

@Composable
private fun DefaultButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colors.secondary,
                shape = CircleShape
            )
            .background(
                color = MaterialTheme.colors.primary,
                shape = CircleShape,
            )
            .clip(shape = CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun RepeatButton(
    modifier: Modifier = Modifier,
    loopType: LoopType,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                shape = AbsoluteRoundedCornerShape(50.dp),
                color = MaterialTheme.colors.primary
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colors.secondary,
                shape = AbsoluteRoundedCornerShape(50.dp)
            ).clip(shape = CircleShape)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = when (loopType) {
                LoopType.NONE -> "반복 안 함"
                LoopType.ONLY_ONE -> "이 노래만 반복"
                LoopType.ALL -> "모든 노래 반복"
            },
            modifier = Modifier.padding(10.dp),
            color = MaterialTheme.colors.secondary,
            fontSize = 14.dpToSp(),
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamily
        )
    }
}

@Composable
private fun PlayButton(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    DefaultButton(
        modifier = modifier,
        onClick = onClick
    ) {
        if (isPlaying) {
            PauseSymbol()
        } else {
            PlaySymbol(modifier = Modifier.size(30.dp))
        }
    }
}

@Composable
private fun ProgressButton(
    modifier: Modifier = Modifier,
    isNext: Boolean,
    onClick: () -> Unit
) {
    DefaultButton(
        modifier = modifier,
        onClick = onClick
    ) {
        ProgressSymbol(isNext = isNext)
    }
}

@Composable
private fun PlaySymbol(
    modifier: Modifier = Modifier
) {
    val triangleShape = GenericShape { size, _ ->
        moveTo(size.width, size.height / 2f)
        lineTo(0f, 0f)
        lineTo(0f, size.height)
    }

    Spacer(
        modifier = modifier
            .clip(triangleShape)
            .background(color = MaterialTheme.colors.secondary)
    )
}

@Composable
private fun PauseSymbol(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.wrapContentWidth()
    ) {
        Box(
            modifier = Modifier
                .width(12.dp)
                .height(30.dp)
                .background(color = MaterialTheme.colors.secondary)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Box(
            modifier = Modifier
                .width(12.dp)
                .height(30.dp)
                .background(color = MaterialTheme.colors.secondary)
        )
    }
}

@Composable
private fun ProgressSymbol(
    modifier: Modifier = Modifier,
    isNext: Boolean
) {
    Row(
        modifier = modifier.rotate(
            if (isNext) 180f else 0f
        )
    ) {
        PlaySymbol(modifier = Modifier.size(15.dp))
        Spacer(modifier = Modifier.width(5.dp))
        PlaySymbol(modifier = Modifier.size(15.dp))
    }
}