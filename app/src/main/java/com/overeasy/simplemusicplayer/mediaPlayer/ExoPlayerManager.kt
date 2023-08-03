@file:OptIn(UnstableApi::class)

package com.overeasy.simplemusicplayer.mediaPlayer

import android.app.Application
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.ExoPlayer.Builder
import com.overeasy.simplemusicplayer.model.LoopType
import com.overeasy.simplemusicplayer.room.entity.MusicData
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.text.Collator
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ExoPlayerManager @Inject constructor(
    private val application: Application
) {
    private var exoPlayer: ExoPlayer? = null
    private val contentResolver = application.contentResolver

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

    fun prepare(
        onFinishScan: (List<MusicData>) -> Unit
    ) {
        scanMusicFiles().sortedWith(
            compareBy(Collator.getInstance(Locale.getDefault())) { musicData ->
                musicData.name
            }
        ).let { musicDataList ->
            exoPlayer?.setMediaItems(
                musicDataList.map { musicData ->
                    MediaItem.fromUri(musicData.path.toUri())
                }
            )
            onFinishScan(musicDataList)
        }
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

    private fun scanMusicFiles(): List<MusicData> = getContentResolverQueryCursor()?.run {
        if (moveToFirst()) {
            val idColumn = getColumnIndex(Media._ID)
            val displayNameColumn = getColumnIndex(Media.DISPLAY_NAME)
            val durationColumn = getColumnIndex(Media.DURATION)
            val pathColumn = getColumnIndex(Media.DATA)

            val musicDataList = arrayListOf<MusicData>()

            do {
                val id = getLong(idColumn)
                val name = getString(displayNameColumn)
//                val artist = getString(artistColumn)
                val duration = getLong(durationColumn)
                val path = getString(pathColumn)

                musicDataList.add(
                    MusicData(
                        id = id,
                        name = name.replace(".${name.takeLastWhile { char -> char != '.' }}", ""),
                        duration = duration,
                        path = path
                    )
                )
            } while (moveToNext())

            musicDataList.apply {
                close()
            }
        } else {
            listOf()
        }
    } ?: listOf()

    private fun getExtSdCardPaths(context: Context): String {
        val paths: MutableList<String> = ArrayList()

        val systemExternalDir = context.getExternalFilesDir("external")

        for (file in context.getExternalFilesDirs("external")) {
            if (file != systemExternalDir) {
                val index = file.absolutePath.lastIndexOf("/Android/data")

                if (index >= 0) {
                    var path = file.absolutePath.substring(0, index)
                    try {
                        path = File(path).canonicalPath
                    } catch (e: IOException) {
                        // Keep non-canonical path.
                    }
                    paths.add(path)
                }
            }
        }

        return paths.firstOrNull() ?: Environment.getExternalStorageDirectory().absolutePath
    }

    private fun getContentResolverQueryCursor() = contentResolver.query(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            Media.EXTERNAL_CONTENT_URI
        },
        arrayOf(
            Media._ID,
            Media.DISPLAY_NAME,
            Media.DURATION,
            Media.DATA
        ),
        "${Media.IS_MUSIC} != ?",
        arrayOf(
            "0"
        ),
        Media.DISPLAY_NAME + " ASC"
    )
}