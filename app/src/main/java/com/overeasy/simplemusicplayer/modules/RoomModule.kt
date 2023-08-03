package com.overeasy.simplemusicplayer.modules

import com.overeasy.simplemusicplayer.room.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideMusicDataDao(appDatabase: AppDatabase) =
        appDatabase.musicDataDao()
}