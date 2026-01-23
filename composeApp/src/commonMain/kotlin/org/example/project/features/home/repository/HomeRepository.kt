package org.example.project.features.home.repository

import org.example.project.core.result.DataError
import org.example.project.core.result.Result
import org.example.project.domain.models.Room

interface HomeRepository {
    suspend fun createRoom(nickName: String, avatar: String):Result<Room,DataError>
    suspend fun joinRoom(nickName: String, avatar: String, roomId: String):Result<Room,DataError>
}