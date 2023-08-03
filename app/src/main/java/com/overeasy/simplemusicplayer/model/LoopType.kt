package com.overeasy.simplemusicplayer.model

import androidx.media3.common.Player

enum class LoopType(val value: Int) {
    NONE(Player.REPEAT_MODE_OFF),
    ALL(Player.REPEAT_MODE_ALL),
    ONLY_ONE(Player.REPEAT_MODE_ONE)
}

fun Int.getLoopTypeByValue() = when (this) {
    LoopType.ALL.value -> LoopType.ALL
    LoopType.ONLY_ONE.value -> LoopType.ONLY_ONE
    else -> LoopType.NONE
}