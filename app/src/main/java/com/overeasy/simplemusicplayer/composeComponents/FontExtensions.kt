package com.overeasy.simplemusicplayer.composeComponents

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun Int.dpToSp(): TextUnit = LocalDensity.current.run { this@dpToSp.dp.toSp() }

@Composable
fun Float.dpToSp(): TextUnit = LocalDensity.current.run { this@dpToSp.dp.toSp() }

@Composable
fun Dp.dpToSp(): TextUnit = LocalDensity.current.run { this@dpToSp.toSp() }