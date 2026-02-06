package org.example.project.presentation.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class NavRoutes {
    @Serializable
    data object Splash : NavRoutes()

    @Serializable
    data object Home : NavRoutes()

    @Serializable
    data class PlayRoom(val reconnectId:String) : NavRoutes()

    @Serializable
    data object Game : NavRoutes()

    @Serializable
    data class WaitingRoom(val reconnectId: String) : NavRoutes()

}



