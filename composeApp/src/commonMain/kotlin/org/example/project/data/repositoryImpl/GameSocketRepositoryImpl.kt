package org.example.project.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import org.example.project.data.remote.socket.SocketEngine
import org.example.project.domain.models.GameMessage
import org.example.project.domain.models.GameMessageEnvelope
import org.example.project.domain.repository.GameSocketRepository

class GameSocketRepositoryImpl(private val socketEngine: SocketEngine):GameSocketRepository {
    override val event: SharedFlow<GameMessageEnvelope> =socketEngine.events
    override suspend fun connect(roomId:String){
        socketEngine.connect(roomId)
    }

    override suspend fun disconnect(roomId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun send(message: GameMessage<*>, destination:String){
        socketEngine.send(message,destination)
    }

}