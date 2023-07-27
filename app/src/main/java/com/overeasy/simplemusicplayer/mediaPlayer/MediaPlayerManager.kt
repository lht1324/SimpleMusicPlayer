package com.overeasy.simplemusicplayer.mediaPlayer

import android.R.attr.path
import android.app.Activity
import android.content.ContentResolver
import android.database.Cursor
import android.media.MediaPlayer
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MediaPlayerManager(
    private val contentResolver: ContentResolver,
    private val lifecycleScope: LifecycleCoroutineScope
) {
    private var mediaPlayer: MediaPlayer? = null


    val isPlaying: Boolean
        get() = (mediaPlayer?.isPlaying ?: Boolean) as Boolean

    init {
        lifecycleScope.launch(Dispatchers.IO) {
            mediaPlayer = MediaPlayer()

//            scanMusicFiles()?.forEach { id ->
//                Log.d("jaehoLee", "id = $id")
//            }

//            mediaPlayer?.prepareAsync()
        }
    }

    private suspend fun scanMusicFiles(): List<Long>? {
        val audioCursor: Cursor? = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            MediaStore.Audio.Media.DATA + " LIKE ? AND " + MediaStore.Audio.Media.DATA + " NOT LIKE ?",
            arrayOf<String>(
                path.toString() + "%", path.toString() + "%/%"
            ),
            MediaStore.Audio.Media.DISPLAY_NAME + " ASC"
        )

        return if (audioCursor != null && audioCursor.moveToFirst()) {
            val idColumn = audioCursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val fileIds: MutableList<Long> = ArrayList()

            do {
                val id = audioCursor.getLong(idColumn)
                fileIds.add(id)
            } while (audioCursor.moveToNext())

            fileIds.apply {
                audioCursor.close()
            }
        } else {
            null
        }
    }

    fun start() {
        mediaPlayer?.start()
    }

    fun stop() {
        mediaPlayer?.stop()
    }

    fun moveTo(second: Int) {
        mediaPlayer?.seekTo(second)
    }

    fun release() {
        lifecycleScope.launch {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
}