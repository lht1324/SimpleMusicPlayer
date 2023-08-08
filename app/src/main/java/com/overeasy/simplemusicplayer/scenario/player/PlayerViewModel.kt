package com.overeasy.simplemusicplayer.scenario.player

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.overeasy.simplemusicplayer.appConfig.MainApplication.Companion.appPreference
import com.overeasy.simplemusicplayer.mediaPlayer.ExoPlayerManager
import com.overeasy.simplemusicplayer.model.LoopType
import com.overeasy.simplemusicplayer.model.getLoopTypeByValue
import com.overeasy.simplemusicplayer.room.entity.MusicData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val exoPlayerManager: ExoPlayerManager
) : ViewModel() {
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

            val progress = currentPosition.toFloat() / musicDataList[currentIndex].duration.toFloat()

            emit(progress)
            delay(1000L)
        }
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

    override fun onCleared() {
        super.onCleared()

        exoPlayerManager.release()
    }

    fun prepare() {
        if (musicDataList.isEmpty()) {
            exoPlayerManager.addListener(
                object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)

                        if (isPlaying)
                            _isPlayingState.value = true
                    }
                }
            )
            exoPlayerManager.prepare(
                onFinishScan = { musicDataList ->
                    _musicDataList.addAll(
                        musicDataList
                    )
                }
            )
        }
    }

    fun release() {
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