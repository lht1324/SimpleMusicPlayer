package com.overeasy.simplemusicplayer.model

sealed class RootRoutes(val route: String) {
    object Player : RootRoutes("player")
    object Setting : RootRoutes("setting")
}
