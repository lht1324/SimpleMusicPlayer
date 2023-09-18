package com.overeasy.simplemusicplayer.scenario.main.player

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.overeasy.simplemusicplayer.appConfig.MainApplication.Companion.appPreference
import com.overeasy.simplemusicplayer.mediaPlayer.ExoPlayerManager
import com.overeasy.simplemusicplayer.model.LoopType
import com.overeasy.simplemusicplayer.model.getLoopTypeByValue
import com.overeasy.simplemusicplayer.room.dao.MusicDataDao
import com.overeasy.simplemusicplayer.room.entity.MusicData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.text.Collator
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val exoPlayerManager: ExoPlayerManager,
    private val musicDataDao: MusicDataDao
) : ViewModel() {
    private val isPlayingListener by lazy {
        object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)

                if (isPlaying)
                    _isPlayingState.value = true
            }
        }
    }

    private val _musicDataList = mutableStateListOf<MusicData>()
    val musicDataList = _musicDataList

    private val _isPlayingState = mutableStateOf(false)
    val isPlayingState = _isPlayingState

    private val _loopType = MutableStateFlow(appPreference.loopType.getLoopTypeByValue())
    val loopType = _loopType.asStateFlow()

    val progressFlow = flow {
        while(true) {
            val currentPosition = exoPlayerManager.currentPosition
            val currentIndex = exoPlayerManager.currentIndex

            emit(currentPosition to currentIndex)
            delay(100L)
        }
    }.filter {
        musicDataList.isNotEmpty()
    }.map { (currentPosition, currentIndex) ->
        currentPosition.toFloat() / musicDataList[currentIndex].duration.toFloat()
    }.filter { progress ->
        !progress.toString().contains("E") && progress in 0.0f..1.0f
    }

    init {
        viewModelScope.launch {
            loopType.collectLatest { type ->
                exoPlayerManager.setLoopType(type)
            }
        }
    }

    fun prepare() {
        viewModelScope.launch {
            if (musicDataList.isEmpty()) {
                exoPlayerManager.addListener(isPlayingListener)

                val dataList = musicDataDao.getAll().sortedWith(
                    compareBy(Collator.getInstance(Locale.getDefault())) { musicData ->
                        musicData.name
                    }
                )

                exoPlayerManager.prepare(
                    mediaItemList = dataList.map { musicData ->
                        MediaItem.fromUri(musicData.path.toUri())
                    }
                )
                _musicDataList.addAll(dataList)
            }
        }
    }

    fun release() {
        exoPlayerManager.removeListener(isPlayingListener)
        exoPlayerManager.release()
    }

    fun onClickItem(isPlaying: Boolean, index: Int) {
        exoPlayerManager.seekToItem(index)

        if (!isPlaying) {
            exoPlayerManager.start()
        }
    }

    fun onClickPlay(isPlaying: Boolean) {
        if (isPlaying) {
            exoPlayerManager.pause()
            _isPlayingState.value = false
        } else {
            exoPlayerManager.start()
        }
    }

    fun onClickPrevious() {
        if (getCurrentSecond() >= 5)
            exoPlayerManager.seekTo(0)
        else
            exoPlayerManager.seekToPrevious()
    }

    fun onClickNext() {
        exoPlayerManager.seekToNext()
    }

    fun onProgressBarDragged(progress: Float) {
        val targetPosition = (musicDataList[exoPlayerManager.currentIndex].duration * progress).toLong()
        exoPlayerManager.seekTo(targetPosition)
    }

    fun onClickRepeat(currentLoopType: LoopType) {
        val nextLoopType = when (currentLoopType) {
            LoopType.NONE -> LoopType.ALL
            LoopType.ALL -> LoopType.ONLY_ONE
            LoopType.ONLY_ONE -> LoopType.NONE
        }

        appPreference.loopType = nextLoopType.value
        _loopType.value = nextLoopType
    }

    private fun getCurrentSecond() = (exoPlayerManager.currentPosition / 1000L).toInt()
}