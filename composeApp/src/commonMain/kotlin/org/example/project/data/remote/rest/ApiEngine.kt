package org.example.project.data.remote.rest

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import org.example.project.domain.models.JoinRoomResponse
import org.example.project.domain.models.Room
import org.example.project.data.remote.ApiRoutes

class ApiEngine(private val client: HttpClient) {


    suspend fun createRoom(nickName:String, avatar:String): JoinRoomResponse {

        val response=client.post(ApiRoutes.CREATE_ROOM_API){
            contentType(ContentType.Application.Json)
            parameter(key="name", value = "room")
            parameter(key="nickName", value = nickName)
            parameter(key="avatar", value = avatar)
        }
        if(!response.status.isSuccess()) {
            throw ClientRequestException(
                response,
                cachedResponseText = response.bodyAsText()
            )
        }
        return response.body<JoinRoomResponse>()
    }

    suspend fun joinRoom(nickName:String, avatar:String,roomId:String): JoinRoomResponse {

        val response=client.post(ApiRoutes.JOIN_ROOM_API(roomId)){
            contentType(ContentType.Application.Json)
            parameter(key="nickName", value = nickName)
            parameter(key="avatar", value = avatar)
        }
        if(!response.status.isSuccess()) {
            throw ClientRequestException(
                response,
                cachedResponseText = response.bodyAsText()
            )
        }
        return response.body<JoinRoomResponse>()
    }

    suspend fun getRoom(roomId:String): Room {

        val response=client.get(ApiRoutes.GET_ROOM_API(roomId)){
            contentType(ContentType.Application.Json)
        }
        if(!response.status.isSuccess()) {
            throw ClientRequestException(
                response,
                cachedResponseText = response.bodyAsText()
            )
        }
        return response.body<Room>()
    }






}