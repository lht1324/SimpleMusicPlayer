package com.overeasy.simplemusicplayer.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SettingData(
    @PrimaryKey val key: Int = 0,
    val isDarkTheme: Boolean = false
)
