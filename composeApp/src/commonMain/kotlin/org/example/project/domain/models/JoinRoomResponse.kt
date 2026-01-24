package org.example.project.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class JoinRoomResponse(
    val room:Room,
    val playerId:String,
    val reconnectToken:String
)