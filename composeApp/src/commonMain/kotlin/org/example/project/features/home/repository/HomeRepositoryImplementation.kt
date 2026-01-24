package org.example.project.features.home.repository

import org.example.project.core.result.Result
import org.example.project.core.result.DataError
import org.example.project.core.utils.safeApiCall
import org.example.project.domain.models.JoinRoomResponse
import org.example.project.domain.models.Room
import org.example.project.serverRoom.rest.ApiEngine

class HomeRepositoryImplementation(private val ApiEngine: ApiEngine):HomeRepository {
    override suspend fun createRoom(nickName: String, avatar: String):Result<JoinRoomResponse,DataError> {

        return safeApiCall {
            ApiEngine.createRoom(nickName, avatar)
        }

    }

    override suspend fun joinRoom(
        nickName: String,
        avatar: String,
        roomId: String
    ): Result<JoinRoomResponse, DataError> {
        return safeApiCall {
            ApiEngine.joinRoom(
                nickName,
                avatar,
                roomId
            )
        }
    }
}