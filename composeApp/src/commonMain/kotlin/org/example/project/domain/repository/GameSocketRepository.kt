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
    suspend fun sendReady(playerId:String,roomId: String)

    suspend fun startGame(playerId: String, roomId: String)

    suspend fun placeBet(playerId: String, roomId: String, bet: Int)

    suspend fun playCard(playerId: String, roomId: String, card: String)

    suspend fun sendChat(playerId: String, roomId: String, message: String)

    suspend fun sendReaction(playerId: String, roomId: String, reaction: String)
}