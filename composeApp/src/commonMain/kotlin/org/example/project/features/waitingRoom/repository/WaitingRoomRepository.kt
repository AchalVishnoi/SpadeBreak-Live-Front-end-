package org.example.project.features.waitingRoom.repository

import org.example.project.core.result.DataError
import org.example.project.core.result.Result
import org.example.project.domain.models.Room

interface WaitingRoomRepository {
    suspend fun getRoom(roomId:String):Result<Room,DataError>
}