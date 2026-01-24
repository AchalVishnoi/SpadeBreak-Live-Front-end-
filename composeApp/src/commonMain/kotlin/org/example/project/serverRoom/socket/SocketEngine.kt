package org.example.project.serverRoom.socket

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.project.domain.models.GameMessage
import org.example.project.domain.models.GameMessageEnvelope
import org.example.project.domain.models.MessageType
import org.example.project.serverRoom.ApiRoutes

class SocketEngine(private val client: HttpClient) {
    private var session:WebSocketSession? = null

    private val _events = MutableSharedFlow<GameMessageEnvelope>()
    val events = _events.asSharedFlow()

    private var listenJob: Job? = null

    private val socketScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    companion object{
        val json=Json{
            ignoreUnknownKeys=true
            explicitNulls=false
        }
    }

    suspend fun connect(roomId: String) {
        session = client.webSocketSession {
            url(ApiRoutes.WEBSOCKET_BASE_URL)
        }

        println("connect fun called")

        listenJob =  socketScope.launch {
            listen(roomId)
        }

        sendStompConnect()
    }



    private suspend fun listen(roomId: String) {
        val currentSession = session ?: return
        println("listen fun called")

        try {
            for (frame in currentSession.incoming) {
                println("Listener alive, frame received")
                if (frame !is Frame.Text) continue

                val text = frame.readText()
                println("[$text]")

                if (text.startsWith("CONNECTED")) {
                    println("STOMP CONNECTED")
                    subscribeRoom(roomId)
                }

                if (text.startsWith("MESSAGE")) {
                    val body = text.substringAfter("\n\n").trimEnd('\u0000')

                    val gameMessage = json.decodeFromString<GameMessageEnvelope>(body)

                    _events.emit(gameMessage)

                    println(text)
                }
            }
        }
        catch (e :Exception){
            println("socket engine exception: ${e.message}")
            connect(roomId)
        }


    }


    suspend fun send(message: GameMessage<*>,destination:String) {
        val stompFrame = """
        SEND
        destination:$destination
        content-type:application/json

        ${json.encodeToString(message)}
        \u0000
    """.trimIndent()

        session?.send(Frame.Text(stompFrame))


    }

    suspend fun disconnect() {
        listenJob?.cancel()
        session?.close()
        session = null
    }


    private suspend fun sendStompConnect() {
        val frame = "CONNECT\n" +
                "accept-version:1.2\n" +
                "host:localhost\n" +
                "\n" +
                "\u0000"

        session?.send(Frame.Text(frame))
    }

    private suspend fun subscribeRoom(roomId: String) {
        println("subscribeRoom fun called")
        val frame =
            "SUBSCRIBE\n" +
                    "id:room-$roomId\n" +
                    "destination:/topic/room/$roomId\n" +
                    "\n" +
                    "\u0000"

        session?.send(Frame.Text(frame))
    }




}