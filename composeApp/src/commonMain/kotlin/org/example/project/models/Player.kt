package org.example.project.models

import kotlinx.serialization.Serializable


@Serializable
data class Player(
    val id:String,
    val nickName:String,
    val host:Boolean,
    val isReady:Boolean,
    val avatar:String
)
