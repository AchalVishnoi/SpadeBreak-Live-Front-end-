package org.example.project.presentation.features.home.presentation

import org.example.project.components.Avatar

sealed class HomeIntent {
    data class NickNameChanged(val nickName:String) : HomeIntent()
    data class AvatarChanged(val avatar: Avatar) : HomeIntent()
    data object JoinRoomClicked : HomeIntent()
    data object CancelClicked : HomeIntent()
    data class RoomIdChanged(val roomId:String) : HomeIntent()
    data object CreateRoomClicked : HomeIntent()
    data class LeaveRoom(val reconnectToken:String):HomeIntent()
}