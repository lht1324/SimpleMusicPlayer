package com.overeasy.simplemusicplayer.composeComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.overeasy.simplemusicplayer.ui.fontFamily

@Composable
fun Header(
    modifier: Modifier = Modifier,
    title: String = "",
    doesShowDivider: Boolean = false,
    startContent: @Composable () -> Unit = { },
    endContent: @Composable () -> Unit = { }
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 18.dp)
                    .align(Alignment.CenterStart)
            ) {
                startContent()
            }
            Text(
                text = title,
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colors.secondary,
                fontSize = 30.dpToSp(),
                fontWeight = FontWeight.Bold,
                fontFamily = fontFamily
            )
            Box(
                modifier = Modifier
                    .padding(end = 18.dp)
                    .align(Alignment.CenterEnd)
            ) {
                endContent()
            }
        }
        if (doesShowDivider) {
            Divider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp,
                color = MaterialTheme.colors.secondary
            )
        }
    }
}