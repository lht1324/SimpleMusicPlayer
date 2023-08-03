package com.overeasy.simplemusicplayer.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.overeasy.simplemusicplayer.room.dao.MusicDataDao
import com.overeasy.simplemusicplayer.room.dao.SettingDao
import com.overeasy.simplemusicplayer.room.entity.MusicData
import com.overeasy.simplemusicplayer.room.entity.SettingData

@Database(
    entities = [MusicData::class, SettingData::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun musicDataDao(): MusicDataDao

    abstract fun settingDao(): SettingDao
}