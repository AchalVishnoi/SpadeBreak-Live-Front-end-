package org.example.project.presentation.features.waitingRoom.presentation

import org.example.project.domain.models.Room

data class WaitingRoomUiState(
    val room:Room?=null,
    val playerId:String?=null,
    val isLoading:Boolean=false,
)