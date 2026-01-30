package org.example.project.domain.models

import kotlinx.serialization.Serializable


@Serializable
data class Game(
    val score:Map<String,Double> = emptyMap(),
    val round:Int=5,
    val roundState: RoundState = RoundState()
)
