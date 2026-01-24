package org.example.project.serverRoom

object ApiRoutes {

    const val HTTP_BASE_URL="https://beaches-adjusted-recreational-north.trycloudflare.com/api/rooms"
    const val WEBSOCKET_BASE_URL="wss://beaches-adjusted-recreational-north.trycloudflare.com/ws"

    const val CREATE_ROOM_API="${HTTP_BASE_URL}"
    fun JOIN_ROOM_API(roomId:String):String="${HTTP_BASE_URL}/${roomId}/join"
    fun GET_ROOM_API(roomId:String):String="${HTTP_BASE_URL}/${roomId}"




    //message destinations

    const val PLAY_CARD="/app/game/playCard"
    const val PLACE_BET="/app/game/placeBet"
    const val READY="/app/game/ready"
    const val START_GAME="/app/game/startGame"
    const val CHAT="/app/game/chat"
    const val REACTION="/app/game/reaction"


}