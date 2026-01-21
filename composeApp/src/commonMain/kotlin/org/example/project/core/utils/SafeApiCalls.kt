package org.example.project.core.utils


import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.SerializationException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.json.Json
import org.example.project.core.result.DataError

import org.example.project.core.result.Result


suspend fun <T> safeApiCall(call: suspend () -> T): Result<T, DataError> {
    return try {
        val result = call()
         Result.Success(result)
    } catch (e: ClientRequestException) {
        val errorText = e.response.bodyAsText()
        println("ClientRequestException: ${e.response.status}")
        println("Error Body from safe api call: $errorText")

        val serverError = try {
            println("Exception type: ${e::class.simpleName}, message: ${e.message}")
            Json.decodeFromString(ApiErrorResponse.serializer(), errorText).message
        } catch (ex: Exception) {
            println("Exception type: ${e::class.simpleName}, message: ${e.message}")
            null
        }

        val error = when (e.response.status) {
            HttpStatusCode.BadRequest -> if (serverError != null)
                                         DataError.Remote.ServerMessage(serverError)
                                         else DataError.Remote.BAD_REQUEST

            HttpStatusCode.NotFound -> DataError.Remote.NOT_FOUND
            HttpStatusCode.TooManyRequests -> DataError.Remote.TOO_MANY_REQUESTS
            HttpStatusCode.Unauthorized -> if (serverError != null)
                                          DataError.Remote.ServerMessage(serverError)
                                          else DataError.Remote.UNAUTHORIZED_ERROR
            HttpStatusCode.Forbidden,
            HttpStatusCode.InternalServerError,
            HttpStatusCode.ServiceUnavailable,
            HttpStatusCode.UnprocessableEntity -> DataError.Remote.SERVER_ERROR

            HttpStatusCode.GatewayTimeout -> DataError.Remote.TIMEOUT_ERROR
            HttpStatusCode.NoContent,
            HttpStatusCode.MovedPermanently -> DataError.Remote.UNKNOWN

            else -> DataError.Remote.UNKNOWN
        }

        println("Exception type: ${e::class.simpleName}, message: ${e.message}")



        Result.Error(error)
    } catch (e: ServerResponseException) {
        println("ServerResponseException: ${e.response.status}")
        println("Error Body from safe api call: ${e.response.bodyAsText()}")
        Result.Error(DataError.Remote.SERVER_ERROR)
    } catch (e: RedirectResponseException) {
        println("RedirectResponseException: ${e.response.status}")
        println("Error Body from safe api call: ${e.response.bodyAsText()}")
        Result.Error(DataError.Remote.REDIRECT_ERROR)
    } catch (e: ConnectTimeoutException) {
        println("ConnectTimeoutException: ${e.message}")
        Result.Error(DataError.Remote.TIMEOUT_ERROR)
    } catch (e: SocketTimeoutException) {
        println("SocketTimeoutException: ${e.message}")
        Result.Error(DataError.Remote.TIMEOUT_ERROR)
    } catch (e: IOException) {
        println("IOException: ${e.message}")
        Result.Error(DataError.Remote.NO_INTERNET)
    } catch (e: SerializationException) {
        println("SerializationException: ${e::class.simpleName}, message: ${e.message}")
        e.printStackTrace()
        Result.Error(DataError.Remote.SERVER_ERROR) // or a custom DataError.Remote.PARSING_ERROR
    } catch (e: Exception) {
        println("Unknown Exception caught in safeApiCall")
        println("Type: ${e::class.qualifiedName}")
        println("Message: ${e.message}")
        e.printStackTrace()
        Result.Error(DataError.Remote.UNKNOWN)
    }
}

@kotlinx.serialization.Serializable
data class ApiErrorResponse(
    val status: Int,
    val message: String,
    val developerMessage: String
)
