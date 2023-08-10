package com.overeasy.simplemusicplayer.scenario.main.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overeasy.simplemusicplayer.room.dao.SettingDao
import com.overeasy.simplemusicplayer.room.entity.SettingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingDao: SettingDao
) : ViewModel() {
    private val settingData = settingDao.getSettingDataFlow().filterNotNull()

    val isDarkTheme = settingData.map { data ->
        data.isDarkTheme
    }

    init {
        viewModelScope.launch {
            if (settingDao.getTableCount() == 0) {
                settingDao.insertSettingData(SettingData())
            }
        }
    }

    fun onClickUpdateIsDarkTheme(updatedIsDarkTheme: Boolean) {
        viewModelScope.launch {
            settingDao.updateIsDarkTheme(updatedIsDarkTheme)
        }
    }
}