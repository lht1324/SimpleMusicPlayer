package com.overeasy.simplemusicplayer.scenario.main

import androidx.lifecycle.ViewModel
import com.overeasy.simplemusicplayer.room.dao.SettingDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingDao: SettingDao
): ViewModel() {
    val isDarkTheme = settingDao
        .getIsDarkThemeFlow()
        .filterNotNull()
}