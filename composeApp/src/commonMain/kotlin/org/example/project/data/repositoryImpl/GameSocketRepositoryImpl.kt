package org.example.project.data.repositoryImpl

import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.serialization.encodeToString
import org.example.project.data.remote.socket.SocketEngine
import org.example.project.data.remote.socket.SocketEngine.Companion.json
import org.example.project.domain.models.GameMessage
import org.example.project.domain.models.GameMessageEnvelope
import org.example.project.domain.models.MessageType
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

    }

    override suspend fun sendReady(playerId: String, roomId: String) {
        val msg = GameMessage<Unit>(
            type = MessageType.PLAYER_IS_READY,
            playLoad = Unit,
            playerId = playerId,
            roomId = roomId,
            timeStamp = getTimeMillis()
        )
        println("sending ready message")
        socketEngine.sendRaw(json.encodeToString(msg), "/app/game/ready")
    }

    override suspend fun startGame(playerId: String, roomId: String) {
        val msg = GameMessage<Unit>(
            type = MessageType.START,
            playLoad = Unit,
            playerId = playerId,
            roomId = roomId,
            timeStamp = getTimeMillis()
        )

        socketEngine.sendRaw(json.encodeToString(msg), "/app/game/startgame")
    }

    override suspend fun placeBet(playerId: String, roomId: String, bet: Int) {
        val msg = GameMessage<Int>(
            type = MessageType.BET,
            playLoad = bet,
            playerId = playerId,
            roomId = roomId,
            timeStamp = getTimeMillis()
        )

        socketEngine.sendRaw(json.encodeToString(msg), "/app/game/placeBet")
    }

    override suspend fun playCard(playerId: String, roomId: String, card: String) {
        val msg = GameMessage<String>(
            type = MessageType.PLAY_CARD,
            playLoad = card,
            playerId = playerId,
            roomId = roomId,
            timeStamp = getTimeMillis()
        )

        socketEngine.sendRaw(json.encodeToString(msg), "/app/game/playCard")
    }

    override suspend fun sendChat(playerId: String, roomId: String, message: String) {
        val msg = GameMessage<String>(
            type = MessageType.TEXT_MESSAGE,
            playLoad = message,
            playerId = playerId,
            roomId = roomId,
            timeStamp = getTimeMillis()
        )

        socketEngine.sendRaw(json.encodeToString(msg), "/app/game/chat")
    }

    override suspend fun sendReaction(playerId: String, roomId: String, reaction: String) {
        val msg = GameMessage<String>(
            type = MessageType.REACTION,
            playLoad = reaction,
            playerId = playerId,
            roomId = roomId,
            timeStamp = getTimeMillis()
        )

        socketEngine.sendRaw(json.encodeToString(msg), "/app/game/reaction")
    }

}