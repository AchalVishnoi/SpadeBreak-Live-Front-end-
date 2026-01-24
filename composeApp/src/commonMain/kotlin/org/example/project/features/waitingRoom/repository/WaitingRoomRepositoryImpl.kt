package org.example.project.features.waitingRoom.repository

import org.example.project.core.result.DataError
import org.example.project.core.result.Result
import org.example.project.core.utils.safeApiCall
import org.example.project.domain.models.Room
import org.example.project.serverRoom.rest.ApiEngine
import org.example.project.serverRoom.socket.SocketEngine

class WaitingRoomRepositoryImpl(private val apiEngine: ApiEngine):WaitingRoomRepository {
    override suspend fun getRoom(roomId: String): Result<Room, DataError> {
       return safeApiCall {
            apiEngine.getRoom(roomId)
        }
    }

}