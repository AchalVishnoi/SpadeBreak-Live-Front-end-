package org.example.project.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import org.example.project.domain.models.GameMessage
import org.example.project.domain.models.GameMessageEnvelope

interface GameSocketRepository {
    val event: SharedFlow<GameMessageEnvelope>
    suspend fun connect(roomId:String)
    suspend fun disconnect(roomId: String)
    suspend fun send(message: GameMessage<*>, destination:String)
}