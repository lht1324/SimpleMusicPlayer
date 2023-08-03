package com.overeasy.simplemusicplayer.model

enum class LoopType(val value: Int) {
    NONE(0),
    ALL(1),
    ONLY_ONE(2)
}

fun Int.getLoopTypeByValue() = when (this) {
    LoopType.ALL.value -> LoopType.ALL
    LoopType.ONLY_ONE.value -> LoopType.ONLY_ONE
    else -> LoopType.NONE
}