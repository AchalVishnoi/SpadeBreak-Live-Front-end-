package org.example.project.features.waitingRoom.presentation

import org.example.project.core.UiSound

sealed class WaitingRoomEvents {
    data class ShowToast(val message: String) : WaitingRoomEvents()
    data class PlaySound(val uiSound: UiSound):WaitingRoomEvents()
    data object NavigateToPlayRoom:WaitingRoomEvents()
}