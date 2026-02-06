package org.example.project.domain.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement


@Serializable
data class GameMessage<T>(
        val type: MessageType,
        val playerId:String?,
        val roomId:String,
        val playLoad:T,
        val timeStamp:Long
)

@Serializable
data class GameMessageEnvelope(
    val type: MessageType,
    val playerId:String?,
    val roomId:String,
    val playLoad:JsonElement?,
    val timeStamp:Long
)

@Serializable
enum class MessageType{
    ROOM_STATE,
    PLAYER_JOINED,
    PLAYER_LEFT,
    PLAYER_IS_READY,
    NEW_ROUND_STARTED,
    START,
    BET,
    PLAY_CARD,
    ROUND_SCORE_UPDATED,
    GAME_SCORE_UPDATED,
    GAME_COMPLETED,
    ROUND_COMPLETED,

    TEXT_MESSAGE,
    REACTION
}