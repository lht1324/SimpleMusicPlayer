package com.overeasy.simplemusicplayer.modules.permission

import android.Manifest
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class StoragePermissionHelper @Inject constructor(
    @ApplicationContext context: Context
) : PermissionHelper(context) {
    override val permissions = listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
}