package com.overeasy.simplemusicplayer.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.overeasy.simplemusicplayer.room.entity.SettingData
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingDao {
    @Query("SELECT * FROM SettingData")
    suspend fun getSettingData(): SettingData

    @Query("SELECT * FROM SettingData")
    fun getSettingDataFlow(): Flow<SettingData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettingData(settingData: SettingData)

    @Query("SELECT isDarkTheme FROM SettingData")
    suspend fun getIsDarkTheme(): Boolean

    @Query("SELECT isDarkTheme FROM SettingData")
    fun getIsDarkThemeFlow(): Flow<Boolean>

    @Query("UPDATE SettingData SET isDarkTheme = :isDarkTheme")
    suspend fun updateIsDarkTheme(isDarkTheme: Boolean)

    @Query("SELECT COUNT(*) FROM SettingData")
    suspend fun getTableCount(): Int
}