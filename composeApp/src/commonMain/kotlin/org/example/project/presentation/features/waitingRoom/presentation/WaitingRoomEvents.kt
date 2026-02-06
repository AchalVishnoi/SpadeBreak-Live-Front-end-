package org.example.project.presentation.features.waitingRoom.presentation

import org.example.project.presentation.utils.UiSound

sealed class WaitingRoomEvents {
    data class ShowToast(val message: String) : WaitingRoomEvents()
    data class PlaySound(val uiSound: UiSound):WaitingRoomEvents()
    data object NavigateToPlayRoom:WaitingRoomEvents()
    data object NavigateBack:WaitingRoomEvents()

}