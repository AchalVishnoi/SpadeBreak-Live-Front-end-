package org.example.project.features.home.presentation

import org.example.project.components.Avatar

sealed class HomeIntent {
    data class NickNameChanged(val nickName:String) : HomeIntent()
    data class AvatarChanged(val avatar: Avatar) : HomeIntent()
    data object JoinRoomClicked : HomeIntent()
    data object CreateRoomClicked : HomeIntent()
}