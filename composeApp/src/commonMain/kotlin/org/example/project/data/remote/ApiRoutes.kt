package org.example.project.data.remote

object ApiRoutes {

    const val HTTP_BASE_URL="http://SpadeBreakLive.eu-north-1.elasticbeanstalk.com/api/rooms"
    const val WEBSOCKET_BASE_URL="ws://SpadeBreakLive.eu-north-1.elasticbeanstalk.com/ws"

    const val CREATE_ROOM_API="$HTTP_BASE_URL"
    fun JOIN_ROOM_API(roomId:String):String="$HTTP_BASE_URL/${roomId}/join"
    fun GET_ROOM_API(roomId:String):String="$HTTP_BASE_URL/${roomId}"
    fun LEAVE_ROOM_API(reconnect_id:String):String="$HTTP_BASE_URL/${reconnect_id}/leave"
    fun RECONNECT_ROOM_API(reconnect_id:String):String ="${HTTP_BASE_URL}/reconnect/$reconnect_id"
    const val PLAYER_READY_API = "${HTTP_BASE_URL}/ready"



    //message destinations
    const val PLAY_CARD="/app/game/playCard"
    const val PLACE_BET="/app/game/placeBet"
    const val READY="/app/game/ready"
    const val START_GAME="/app/game/startGame"
    const val CHAT="/app/game/chat"
    const val REACTION="/app/game/reaction"



}