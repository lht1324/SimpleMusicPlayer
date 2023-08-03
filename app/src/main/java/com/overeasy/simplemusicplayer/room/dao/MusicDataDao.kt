package com.overeasy.simplemusicplayer.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.overeasy.simplemusicplayer.room.entity.MusicData

@Dao
interface MusicDataDao {
    @Query("SELECT COUNT(*) FROM MusicData")
    suspend fun getMusicDataCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(musicDataList: List<MusicData>)

    @Update
    suspend fun updateAll(musicDataList: List<MusicData>)

    @Query("DELETE FROM MusicData")
    suspend fun deleteAll()

    @Query("SELECT * FROM MusicData")
    suspend fun getAll(): List<MusicData>
}