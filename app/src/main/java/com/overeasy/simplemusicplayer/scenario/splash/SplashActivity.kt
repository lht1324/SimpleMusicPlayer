package com.overeasy.simplemusicplayer.scenario.splash

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.overeasy.simplemusicplayer.modules.permission.StoragePermissionHelper
import com.overeasy.simplemusicplayer.scenario.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {
    private val viewModel: SplashViewModel by viewModels()

    @Inject
    lateinit var permissionHelper: StoragePermissionHelper

    override fun onStart() {
        super.onStart()

        initUI()
    }

    private fun initUI() {
        setContent {
            SplashScreen(
                permissionHelper = permissionHelper,
                startLoading = {
                    viewModel.startLoading(
                        onFinishLoading = {
                            startActivity(
                                Intent(this@SplashActivity, MainActivity::class.java)
                            )
                        }
                    )
                },
                onFinish = this@SplashActivity::finish
            )
        }
    }
}