package com.overeasy.simplemusicplayer.scenario.player

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.overeasy.simplemusicplayer.composeComponents.noRippleClickable
import com.overeasy.simplemusicplayer.ui.Color343434

@Composable
fun MusicController(
    modifier: Modifier = Modifier,
    onClickPrevious: () -> Unit,
    onClickPlay: () -> Unit,
    onClickNext: () -> Unit
) {
    val isPlaying = true

    Column(
        modifier = modifier
    ) {
        ProgressBar()
        Buttons(
            isPlaying = isPlaying,
            onClickPrevious = onClickPrevious,
            onClickPlay = onClickPlay,
            onClickNext = onClickNext
        )
    }
}

@Composable
private fun ProgressBar() {

}

@Composable
private fun Buttons(
    modifier: Modifier = Modifier,
    isPlaying: Boolean,
    onClickPrevious: () -> Unit,
    onClickPlay: () -> Unit,
    onClickNext: () -> Unit
) {
    Row(
        modifier = modifier,
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

@Composable
private fun DefaultButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.background(
            color = Color.Black,
            shape = CircleShape,
        ).border(
            width = 2.dp,
            color = Color343434,
            shape = CircleShape
        ).noRippleClickable(onClick = onClick)
    ) {
        content()
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
            PauseSymbol(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            PlaySymbol(
                modifier = Modifier.align(Alignment.Center)
            )
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
        ProgressSymbol(
            modifier = Modifier.align(Alignment.Center),
            isNext = isNext
        )
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
            .background(color = Color.Black)
    )
}

@Composable
private fun PauseSymbol(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .width(10.dp)
                .height(20.dp)
                .background(color = Color.White)
        )
        Spacer(modifier = Modifier.width(5.dp))
        Box(
            modifier = Modifier
                .width(10.dp)
                .height(20.dp)
                .background(color = Color.White)
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
        PlaySymbol()
        Spacer(modifier = Modifier.width(5.dp))
        PlaySymbol()
    }
}