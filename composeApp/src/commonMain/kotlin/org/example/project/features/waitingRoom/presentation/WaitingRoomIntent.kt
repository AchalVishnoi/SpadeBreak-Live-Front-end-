package org.example.project.features.waitingRoom.presentation

sealed class WaitingRoomIntent{
    data class Connect(val roomId:String):WaitingRoomIntent()
    data object Ready:WaitingRoomIntent()
}