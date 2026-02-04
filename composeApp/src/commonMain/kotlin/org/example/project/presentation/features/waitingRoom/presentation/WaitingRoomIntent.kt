package org.example.project.presentation.features.waitingRoom.presentation

sealed class WaitingRoomIntent{
    data class Connect(val reconnectId:String):WaitingRoomIntent()
    data object Ready:WaitingRoomIntent()
    data class LeaveRoom(val reconnectId: String):WaitingRoomIntent()
}