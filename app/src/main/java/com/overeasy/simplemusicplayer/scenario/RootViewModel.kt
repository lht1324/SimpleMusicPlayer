package com.overeasy.simplemusicplayer.scenario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.overeasy.simplemusicplayer.room.dao.SettingDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    private val settingDao: SettingDao
): ViewModel() {
    val isDarkTheme = settingDao
        .getIsDarkThemeFlow()
        .filterNotNull()
}