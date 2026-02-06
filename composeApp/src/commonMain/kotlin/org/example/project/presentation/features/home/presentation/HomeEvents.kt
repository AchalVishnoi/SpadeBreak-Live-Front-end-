package org.example.project.presentation.features.home.presentation

import org.example.project.domain.models.GameMessage
import org.example.project.domain.models.Room

sealed class HomeEvents {
    data class ShowToast(val message: String):HomeEvents()
    data class NavigateToWaitingRoom(val room: Room, val playerId:String, val reconnectionToken:String):HomeEvents()
}