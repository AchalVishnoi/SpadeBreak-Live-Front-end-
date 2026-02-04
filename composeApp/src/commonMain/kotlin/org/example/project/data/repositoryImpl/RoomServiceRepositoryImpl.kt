package org.example.project.data.repositoryImpl

import org.example.project.data.remote.rest.ApiEngine
import org.example.project.domain.models.JoinRoomResponse
import org.example.project.domain.models.Room
import org.example.project.domain.repository.RoomServiceRepository
import org.example.project.domain.result.DataError
import org.example.project.domain.result.Result
import org.example.project.domain.utils.safeApiCall

class RoomServiceRepositoryImpl(private val api:ApiEngine):RoomServiceRepository {
    override suspend fun createRoom(
        nickName: String,
        avatar: String
    ): Result<JoinRoomResponse, DataError> {
        return safeApiCall {
            api.createRoom(nickName, avatar)
        }
    }

    override suspend fun joinRoom(
        nickName: String,
        avatar: String,
        roomId: String
    ): Result<JoinRoomResponse, DataError> {
       return safeApiCall {
            api.joinRoom(nickName, avatar, roomId)
       }
    }

    override suspend fun getRoom(roomId: String): Result<Room, DataError> {
        return safeApiCall {
            api.getRoom(roomId)
        }
    }

    override suspend fun reconnectRoom(reconnectId: String): Result<JoinRoomResponse, DataError> {
        return safeApiCall {
            api.reconnectRoom(reconnectId)
        }
    }

    override suspend fun leaveRoom(reconnectId: String): Result<Unit, DataError> {
        return safeApiCall {
            api.leaveRoom(reconnectId)
        }
    }

    override suspend fun isReady(playerId: String, roomId: String): Result<Room, DataError> {
        return safeApiCall {
            api.isReady(playerId, roomId)
        }
    }
}