package org.example.project.domain.models

import kotlinx.serialization.Serializable


@Serializable
data class Room(

    val id:String,
    val name:String,
    val status: Status,
    val players:List<Player>,
    val game: Game?=null
)



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
