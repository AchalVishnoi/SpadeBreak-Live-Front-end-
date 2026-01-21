package org.example.project.models

import kotlinx.serialization.Serializable


@Serializable
data class RoundState (
    val roundStatus:RoundStatus,
    val trickNum: Int,
    val score:Map<String,PlayerRoundScore>,
    val handCards:Map<String, List<String>>,
    val centerTrickedCard:Map<String,String>,
    val playerTurn:String,
    val trickLeaderId:String
    )

enum class RoundStatus{
    BETTING,
    PLAYING,
    TRICK_END,
    ROUND_END
}

@Serializable
data class PlayerRoundScore(
    val bet:Int,
    val currPoints:Int
)