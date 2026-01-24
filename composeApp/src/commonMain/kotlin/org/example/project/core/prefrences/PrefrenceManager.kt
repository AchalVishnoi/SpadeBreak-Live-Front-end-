package org.example.project.core.prefrences

import com.russhwolf.settings.Settings

class PrefrenceManager(private val settings: Settings= Settings()) {

    companion object{
        val RECONNECT_TOKEN = "reconnect_token"
        val PREVIONS_ROOMS_HISTORY = "previous_room_history"
    }

    fun saveReconnectToken(token:String){
        settings.putString(RECONNECT_TOKEN,token)
    }

    fun getReconnectToken():String?{
        return settings.getStringOrNull(RECONNECT_TOKEN)
    }

    fun deleteToken(){
        settings.remove(RECONNECT_TOKEN)
    }

    fun clear(){
        settings.clear()
    }

}