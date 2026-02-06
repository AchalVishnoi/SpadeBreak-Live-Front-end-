package org.example.project.domain.repository

import org.example.project.domain.models.JoinRoomResponse
import org.example.project.domain.models.Room
import org.example.project.domain.result.DataError
import org.example.project.domain.result.Result

interface RoomServiceRepository {
    suspend fun createRoom(nickName: String, avatar: String): Result<JoinRoomResponse, DataError>
    suspend fun joinRoom(nickName: String, avatar: String, roomId: String): Result<JoinRoomResponse, DataError>
    suspend fun getRoom(roomId:String): Result<Room, DataError>
    suspend fun reconnectRoom(reconnectId:String):Result<JoinRoomResponse, DataError>
    suspend fun leaveRoom(reconnectId: String):Result<Unit,DataError>
    suspend fun isReady(playerId:String,roomId: String):Result<Room,DataError>
}