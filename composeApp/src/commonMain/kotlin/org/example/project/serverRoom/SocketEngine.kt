package org.example.project.serverRoom

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readBytes
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.example.project.domain.models.GameMessage

class SocketEngine(private val client: HttpClient) {
    private var session:WebSocketSession? = null

    private val _events = MutableSharedFlow<GameMessage<*>>()
    val events = _events.asSharedFlow()

    private var listenJob: Job? = null

    private val socketScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

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

        for (frame in currentSession.incoming) {
            println("Listener alive, frame received")
            if (frame !is Frame.Text) continue

            val text = frame.readText()
            println("WS IN >>> [$text]")

            if (text.startsWith("CONNECTED")) {
                println("STOMP CONNECTED ✔")
                subscribeRoom(roomId)
            }

            if (text.startsWith("MESSAGE")) {
               println(text)
            }
        }
    }


    suspend fun send(message: GameMessage<*>) {

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