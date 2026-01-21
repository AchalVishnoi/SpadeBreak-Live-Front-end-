package org.example.project.models

import kotlinx.serialization.Serializable


@Serializable
data class Game(
    val score:Map<String,Double>,
    val round:Int=5,
    val roundState:RoundState
)
