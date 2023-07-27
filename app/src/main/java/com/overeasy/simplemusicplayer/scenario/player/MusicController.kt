package com.overeasy.simplemusicplayer.scenario.player

import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.overeasy.simplemusicplayer.composeComponents.noRippleClickable
import com.overeasy.simplemusicplayer.ui.Color343434
import com.overeasy.simplemusicplayer.ui.ColorE0E0E0

@Composable
fun MusicController(
    modifier: Modifier = Modifier,
    onClickPrevious: () -> Unit,
    onClickPlay: () -> Unit,
    onClickNext: () -> Unit
) {
//    val isPlaying = false
    var isPlaying by remember { mutableStateOf(false) }
    val progress = 0.3f

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colors.secondary,
            thickness = 1.5.dp
        )
        ProgressBar(
            progress = progress
        )
        Buttons(
            isPlaying = isPlaying,
            onClickPrevious = onClickPrevious,
//            onClickPlay = onClickPlay,
            onClickPlay = {
                isPlaying = !isPlaying
            },
            onClickNext = onClickNext
        )
    }
}

@Composable
private fun ProgressBar(
    progress: Float
) {

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
        modifier = modifier
            .padding(vertical = 30.dp, horizontal = 24.dp)
            .fillMaxWidth(),
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