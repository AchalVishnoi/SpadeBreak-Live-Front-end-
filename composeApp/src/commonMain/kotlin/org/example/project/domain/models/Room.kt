package org.example.project.domain.models

import kotlinx.serialization.Serializable


@Serializable
data class Room(

    val id:String="",
    val name:String="",
    val status: Status=Status.OPEN,
    val players:List<Player> = emptyList(),
    val game: Game?=null
)


@Serializable
enum class Status{
    OPEN,
    READY,
    IN_GAME
}

fun List<Player>.getPlayerById(playerId:String):Player?{
       this.forEach {
          if(it.id==playerId){
              return it
          }
      }
      return null
}
