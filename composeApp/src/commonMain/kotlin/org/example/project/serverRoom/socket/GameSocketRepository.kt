package org.example.project.serverRoom.socket

import org.example.project.domain.models.GameMessage

class GameSocketRepository(private val socketEngine: SocketEngine) {
    val event=socketEngine.events
    suspend fun connect(roomId:String){
        socketEngine.connect(roomId)
    }

    suspend fun disconnect(){
        socketEngine.disconnect()
    }

    suspend fun send(message: GameMessage<*>, destination:String){
        socketEngine.send(message,destination)
    }

}