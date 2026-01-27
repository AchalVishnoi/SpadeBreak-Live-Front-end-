package org.example.project.features.entry.presentation

import org.example.project.components.Avatar
import org.example.project.domain.models.Room

data class HomeUiState(
    val isLoading:Boolean=false,
    val nickName:String="",
    val avatar:Avatar,
    val nickNameFieldError:String?=null,
    val enterdRoomId:String="",
    val room: Room?=null
)