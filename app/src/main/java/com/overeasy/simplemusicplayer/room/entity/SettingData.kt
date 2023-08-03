package com.overeasy.simplemusicplayer.room.entity

import androidx.room.Entity

@Entity
data class SettingData(
    val loopType: Int,
    val isDarkTheme: Boolean = true
)
