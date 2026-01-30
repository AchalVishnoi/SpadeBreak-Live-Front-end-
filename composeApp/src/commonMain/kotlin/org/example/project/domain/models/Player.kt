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