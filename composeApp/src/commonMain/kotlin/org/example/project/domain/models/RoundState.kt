package org.example.project.domain.models

import kotlinx.serialization.Serializable


@Serializable
data class RoundState (
    val status: RoundStatus = RoundStatus.BETTING,
    val trickNum: Int = 0,
    val score:Map<String, PlayerRoundScore> = emptyMap(),
    val handCards:Map<String, List<String>> = emptyMap(),
    val centerTrickedCard:Map<String,String> = emptyMap(),
    val playerTurn:String = "",
    val trickLeaderId:String? = null
    )

@Serializable
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