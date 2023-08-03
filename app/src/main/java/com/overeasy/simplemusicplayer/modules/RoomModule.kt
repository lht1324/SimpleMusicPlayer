package com.overeasy.simplemusicplayer.modules

import android.content.Context
import androidx.room.Room
import com.overeasy.simplemusicplayer.room.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {


    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()

    @Provides
    @Singleton
    fun provideMusicDataDao(appDatabase: AppDatabase) =
        appDatabase.musicDataDao()

    @Provides
    @Singleton
    fun provideSettingDao(appDatabase: AppDatabase) =
        appDatabase.settingDao()
}