package org.example.project.presentation.features.splash

sealed class SplashEvents {
    data object NavigateHome:SplashEvents()
    data class NavigateWaitingRoom(val reconnectToken:String):SplashEvents()
    data class NavigatePlayRoom(val reconnectToken:String):SplashEvents()
}