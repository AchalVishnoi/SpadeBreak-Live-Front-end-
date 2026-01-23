package org.example.project.serverRoom

object ApiRoutes {

    const val HTTP_BASE_URL="https://69a980e165cb.ngrok-free.app/api/rooms"
    const val WEBSOCKET_BASE_URL="wss://69a980e165cb.ngrok-free.app/ws"

    const val CREATE_ROOM_API="${HTTP_BASE_URL}"
    fun JOIN_ROOM_API(roomId:String):String="${HTTP_BASE_URL}/${roomId}/join"



}