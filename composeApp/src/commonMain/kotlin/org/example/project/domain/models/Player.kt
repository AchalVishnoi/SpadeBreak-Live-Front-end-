package org.example.project.domain.models

import kotlinx.serialization.Serializable


@Serializable
data class Player(
    val id:String,
    val nickname:String,
    val host:Boolean,
    val ready:Boolean,
    val avatar:String
)