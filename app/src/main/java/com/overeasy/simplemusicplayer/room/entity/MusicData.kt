package com.overeasy.simplemusicplayer.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MusicData(
    @PrimaryKey val id: Long,
    val name: String,
    val duration: Long,
    val path: String,
    val index: Int = -1
)
