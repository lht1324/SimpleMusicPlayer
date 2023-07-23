package com.overeasy.simplemusicplayer.scenario

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.overeasy.simplemusicplayer.model.RootRoutes
import com.overeasy.simplemusicplayer.scenario.player.PlayerScreen

@Composable
fun RootNavHost(
    navController: NavHostController,
    mediaPlayer: MediaPlayer
) {
    NavHost(
       navController = navController,
        startDestination = RootRoutes.Player.route
    ) {
        composable(RootRoutes.Player.route) {
            PlayerScreen(
                mediaPlayer = mediaPlayer
            )
        }
        composable(RootRoutes.Setting.route) {

        }
    }
}