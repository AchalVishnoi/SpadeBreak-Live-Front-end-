package org.example.project.domain.models

import kotlinx.serialization.Serializable


@Serializable
data class Game(
    val score:List<Map<String,PlayerRoundScore>> = emptyList(),
    val round:Int=5,
    val roundState: RoundState = RoundState()
)

val dummyScore: List<Map<String, PlayerRoundScore>> = listOf(

    mapOf(
        "1" to PlayerRoundScore(bet = 2, currPoints = 3),
        "2" to PlayerRoundScore(bet = 1, currPoints = 4),
        "3" to PlayerRoundScore(bet = 2, currPoints = 1),
        "4" to PlayerRoundScore(bet = 1, currPoints = 5)
    ),
    mapOf(
        "1" to PlayerRoundScore(bet = 2, currPoints = 3),
        "2" to PlayerRoundScore(bet = 1, currPoints = 4),
        "3" to PlayerRoundScore(bet = 2, currPoints = 1),
        "4" to PlayerRoundScore(bet = 1, currPoints = 5)
    ),
    mapOf(
        "1" to PlayerRoundScore(bet = 2, currPoints = 3),
        "2" to PlayerRoundScore(bet = 1, currPoints = 4),
        "3" to PlayerRoundScore(bet = 2, currPoints = 1),
        "4" to PlayerRoundScore(bet = 1, currPoints = 5)
    ),
    mapOf(
        "1" to PlayerRoundScore(bet = 2, currPoints = 3),
        "2" to PlayerRoundScore(bet = 1, currPoints = 4),
        "3" to PlayerRoundScore(bet = 2, currPoints = 1),
        "4" to PlayerRoundScore(bet = 1, currPoints = 5)
    ),
    mapOf(
        "1" to PlayerRoundScore(bet = 2, currPoints = 3),
        "2" to PlayerRoundScore(bet = 1, currPoints = 4),
        "3" to PlayerRoundScore(bet = 2, currPoints = 1),
        "4" to PlayerRoundScore(bet = 1, currPoints = 5)
    ),

)
