package com.overeasy.simplemusicplayer.appConfig

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppPreference @Inject constructor(
    @ApplicationContext context: Context,
    appPreferenceName: String = "appPref_simple_music_player"
) : BaseAppPreference(context, appPreferenceName) {
    companion object {
        private const val KEY_IS_DARK_THEME = "KEY_IS_DARK_THEME"
    }

    var isDarkTheme: Boolean
        get() = getBooleanData(KEY_IS_DARK_THEME, true)
        set(value) {
            setData(KEY_IS_DARK_THEME, value)
        }
}