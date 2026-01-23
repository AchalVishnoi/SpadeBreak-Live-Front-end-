package org.example.project.features.home.presentation

import org.example.project.domain.models.GameMessage
import org.example.project.domain.models.Room

sealed class HomeEvents {
    data class ShowToast(val message: String):HomeEvents()
    data class NavigateToRoom(val room: Room):HomeEvents()
}