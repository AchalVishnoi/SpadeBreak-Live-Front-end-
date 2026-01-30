package org.example.project.presentation.features.splash

sealed class SplashIntent {
    data class LoadRoom(val reconnectToken:String):SplashIntent()
}