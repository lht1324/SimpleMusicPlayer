@file:OptIn(ExperimentalPermissionsApi::class)

package com.overeasy.simplemusicplayer.scenario.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.overeasy.simplemusicplayer.composeComponents.dpToSp
import com.overeasy.simplemusicplayer.modules.permission.StoragePermissionHelper
import com.overeasy.simplemusicplayer.modules.permission.listener.PermissionListener
import com.overeasy.simplemusicplayer.ui.fontFamily

@Composable
fun SplashScreen(
    permissionHelper: StoragePermissionHelper,
    startLoading: () -> Unit,
    onFinish: () -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = permissionHelper.permissions
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primary)
    ) {
        Text(
            text = "테스트",
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colors.secondary,
            fontSize = 30.dpToSp(),
            fontWeight = FontWeight.Bold,
            fontFamily = fontFamily
        )
    }

    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionHelper.requestPermission(
                object : PermissionListener {
                    override fun onPermissionGranted() {
                        startLoading()
                    }

                    override fun onPermissionShouldBeGranted(deniedPermission: List<String>) {
                        // 재권유
                        onFinish()
                    }

                    override fun onAnyPermissionsPermanentlyDenied(
                        deniedPermissions: List<String>,
                        permanentlyDeniedPermissions: List<String>
                    ) {
                        // 설정 열어주기
                        onFinish()
                    }
                }
            )
        } else {
            startLoading()
        }
    }
}