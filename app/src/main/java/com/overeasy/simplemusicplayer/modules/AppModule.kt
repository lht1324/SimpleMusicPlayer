package com.overeasy.simplemusicplayer.modules

import android.app.Application
import com.overeasy.simplemusicplayer.mediaPlayer.ExoPlayerManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMediaPlayerManager(application: Application) =
        ExoPlayerManager(application)
}