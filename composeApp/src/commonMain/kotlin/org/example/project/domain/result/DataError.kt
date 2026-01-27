package org.example.project.domain.result


sealed class DataError : Error {

        sealed class Remote(val message: String? = null) : DataError() {
            object BAD_REQUEST : Remote()
            object UNAUTHORIZED_ERROR : Remote()
            object NOT_FOUND : Remote()
            object TOO_MANY_REQUESTS : Remote()
            object SERVER_ERROR : Remote()
            object REDIRECT_ERROR : Remote()
            object TIMEOUT_ERROR : Remote()
            object NO_INTERNET : Remote()
            object UNKNOWN : Remote()
            data class ServerMessage(val serverMsg: String) : Remote(serverMsg)
        }


    sealed class Local : DataError() {
       object CACHE_NOT_FOUND: Local()
       object FILE_READ_ERROR: Local()
    }
}