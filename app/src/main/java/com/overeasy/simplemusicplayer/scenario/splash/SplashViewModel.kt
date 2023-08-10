package com.overeasy.simplemusicplayer.scenario.splash

import android.app.Application
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overeasy.simplemusicplayer.room.dao.MusicDataDao
import com.overeasy.simplemusicplayer.room.entity.MusicData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val musicDataDao: MusicDataDao,
    private val application: Application
) : ViewModel() {
    fun startLoading(
        onFinishLoading: () -> Unit
    ) {
        viewModelScope.launch {
            scanMusicFiles(
                onFinishScan = onFinishLoading
            )
        }
    }

    private suspend fun scanMusicFiles(
        onFinishScan: () -> Unit
    ) {
        val cursor = getContentResolverQueryCursor()
        val musicDataList = cursor?.run {
            if (moveToFirst()) {
                val idColumn = getColumnIndex(MediaStore.Audio.Media._ID)
                val displayNameColumn = getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
                val durationColumn = getColumnIndex(MediaStore.Audio.Media.DURATION)
                val pathColumn = getColumnIndex(MediaStore.Audio.Media.DATA)

                val dataList = arrayListOf<MusicData>()

                do {
                    val id = getLong(idColumn)
                    val name = getString(displayNameColumn)
                    val duration = getLong(durationColumn)
                    val path = getString(pathColumn)

                    dataList.add(
                        MusicData(
                            id = id,
                            name = name.replace(".${name.takeLastWhile { char -> char != '.' }}", ""),
                            duration = duration,
                            path = path
                        )
                    )
                } while (moveToNext())

                dataList.apply {
                    close()
                }
            } else {
                listOf()
            }
        } ?: listOf()

        musicDataDao.insertAll(musicDataList)

        onFinishScan()
    }


    private fun getContentResolverQueryCursor() = application.contentResolver.query(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            MediaStore.Audio.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        },
        arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
        ),
        "${MediaStore.Audio.Media.IS_MUSIC} != ?",
        arrayOf(
            "0"
        ),
        MediaStore.Audio.Media.DISPLAY_NAME + " ASC"
    )
}