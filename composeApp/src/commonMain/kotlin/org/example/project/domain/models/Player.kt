package org.example.project.domain.models

import kotlinx.serialization.Serializable
import org.example.project.components.Avatar


@Serializable
data class Player(
    val id:String="",
    val nickname:String="",
    val host:Boolean=false,
    val ready:Boolean=false,
    val avatar:String=Avatar.AVATAR_01.id
)

val dummyPlayers = listOf(
    Player(id = "1", nickname = "Achal", host = true, ready = true, avatar = Avatar.AVATAR_01.id),
    Player(id = "2", nickname = "Mohan", ready = true, avatar = Avatar.AVATAR_05.id),
    Player(id = "3", nickname = "Anurag", ready = true, avatar = Avatar.AVATAR_09.id),
    Player(id = "4", nickname = "Ravi", ready = true, avatar = Avatar.AVATAR_12.id)
)
