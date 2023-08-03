package com.overeasy.simplemusicplayer.scenario

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.overeasy.simplemusicplayer.model.RootRoutes
import com.overeasy.simplemusicplayer.scenario.player.PlayerScreen
import com.overeasy.simplemusicplayer.scenario.setting.SettingScreen

@Composable
fun RootNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = RootRoutes.Player.route
    ) {
        composable(RootRoutes.Player.route) {
            PlayerScreen(
                onClickSetting = {
                    navController.navigate(RootRoutes.Setting.route)
                }
            )
        }
        composable(RootRoutes.Setting.route) {
            SettingScreen()
        }
    }
}