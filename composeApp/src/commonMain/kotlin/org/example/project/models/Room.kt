package org.example.project.models

import kotlinx.serialization.Serializable


@Serializable
data class Room(

    val id:String,
    val name:String,
    val status:Status,
    val players:List<Player>,
    val game:Game?=null
)



enum class Status{
    OPEN,
    READY,
    IN_GAME
}