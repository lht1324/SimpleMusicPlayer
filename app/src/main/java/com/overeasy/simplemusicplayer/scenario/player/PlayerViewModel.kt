package com.overeasy.simplemusicplayer.scenario.player

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import com.overeasy.simplemusicplayer.mediaPlayer.ExoPlayerManager
import com.overeasy.simplemusicplayer.room.entity.MusicData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val exoPlayerManager: ExoPlayerManager
) : ViewModel() {
    private val _musicDataList = mutableStateListOf<MusicData>()
    val musicDataList = _musicDataList

    private val _isPlayingState = mutableStateOf(false)
    val isPlayingState = _isPlayingState
    private val _currentPositionState = mutableStateOf(0)
    val currentPositionState = _currentPositionState

    private var currentPositionUpdateJob: Job? = null

    val progressFlow = flow {
        while(true) {
            val currentPosition = exoPlayerManager.currentPosition
            val currentIndex = exoPlayerManager.currentIndex

            val progress = currentPosition.toFloat() / musicDataList[currentIndex].duration.toFloat()

            emit(progress.toFloat())
            delay(1000L)
        }
    }.filter { progress ->
        !progress.toString().contains("E") && progress in 0.0f..1.0f
    }

    init {
        exoPlayerManager.addListener(
            object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)

                    if (isPlaying)
                        _isPlayingState.value = true
                }
            }
        )
    }

    override fun onCleared() {
        super.onCleared()

        exoPlayerManager.release()
    }

    fun prepare() {
        exoPlayerManager.prepare(
            onFinishScan = { musicDataList ->
                _musicDataList.addAll(
                    musicDataList
                )
            }
        )
    }

    fun release() {
        exoPlayerManager.release()
    }

    fun onClickItem(isPlaying: Boolean, index: Int) {
        exoPlayerManager.seekToItem(index)

        if (!isPlaying) {
            exoPlayerManager.start()
//            startTrackingCurrentPosition()
        }
    }

    fun onClickPlay(isPlaying: Boolean) {
        if (isPlaying) {
            exoPlayerManager.pause()
            _isPlayingState.value = false
//            stopTrackingCurrentPosition(false)
        } else {
            exoPlayerManager.start()
//            startTrackingCurrentPosition()
        }
    }

    fun onClickPrevious() {
        if (getCurrentSecond() >= 5)
            exoPlayerManager.seekTo(0)
        else
            exoPlayerManager.seekToPrevious()

//        stopTrackingCurrentPosition(true)
    }

    fun onClickNext() {
        exoPlayerManager.seekToNext()
    }

    fun onProgressBarDragged(progress: Float) {
        val targetPosition = (musicDataList[exoPlayerManager.currentIndex].duration * progress).toLong()
        exoPlayerManager.seekTo(targetPosition)
    }

    private fun moveTo(milliseconds: Long) {
        exoPlayerManager.seekTo(milliseconds)
    }

    private fun startTrackingCurrentPosition() {
        currentPositionUpdateJob = viewModelScope.launch(Dispatchers.IO) {
            delay(1000L)
            _currentPositionState.value = getCurrentSecond()
            Log.d("jaehoLee", "state = ${currentPositionState.value}")
        }
        currentPositionUpdateJob?.start()
    }

    private fun stopTrackingCurrentPosition(isReset: Boolean) {
        if (isReset)
            _currentPositionState.value = 0

        currentPositionUpdateJob?.cancel()
        currentPositionUpdateJob = null
    }

    private fun getCurrentSecond() = (exoPlayerManager.currentPosition / 1000L).toInt()
}