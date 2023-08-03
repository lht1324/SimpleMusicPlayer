package com.overeasy.simplemusicplayer.appConfig

import android.content.Context
import com.overeasy.simplemusicplayer.model.LoopType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppPreference @Inject constructor(
    @ApplicationContext context: Context,
    appPreferenceName: String = "appPref_simple_music_player"
) : BaseAppPreference(context, appPreferenceName) {
    companion object {
        private const val KEY_LOOP_TYPE = "KEY_LOOP_TYPE"
    }

    var loopType: Int
        get() = getIntData(KEY_LOOP_TYPE, LoopType.NONE.value)
        set(value) {
            setData(KEY_LOOP_TYPE, value)
        }
}