@file:OptIn(UnstableApi::class)

package com.overeasy.simplemusicplayer.mediaPlayer

import android.app.Application
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.ExoPlayer.Builder
import com.overeasy.simplemusicplayer.model.LoopType
import javax.inject.Inject

class ExoPlayerManager @Inject constructor(
    private val application: Application
) {
    private var exoPlayer: ExoPlayer? = null

    val currentPosition: Long
        get() = exoPlayer?.currentPosition ?: 0L
    val currentIndex: Int
        get() = exoPlayer?.currentMediaItemIndex ?: 0

    init {
        exoPlayer = Builder(
            application,
            DefaultRenderersFactory(application)
                .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF)
        ).build()
    }

    fun addListener(listener: Player.Listener) {
        exoPlayer?.addListener(listener)
    }

    fun removeListener(listener: Player.Listener) {
        exoPlayer?.removeListener(listener)
    }

    fun prepare(
        mediaItemList: List<MediaItem>
    ) {
        exoPlayer?.setMediaItems(mediaItemList)
        exoPlayer?.prepare()
    }

    fun start() {
        exoPlayer?.play()
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun seekTo(milliseconds: Long) {
        exoPlayer?.seekTo(milliseconds)
    }

    fun seekToItem(index: Int) {
        exoPlayer?.seekTo(index, 0L)
    }

    fun seekToPrevious() {
        exoPlayer?.seekToPrevious()
    }

    fun seekToNext() {
        exoPlayer?.seekToNext()
    }

    fun release() {
        exoPlayer?.release()
        exoPlayer = null
    }

    fun setLoopType(loopType: LoopType) {
        exoPlayer?.repeatMode = loopType.value
    }
}