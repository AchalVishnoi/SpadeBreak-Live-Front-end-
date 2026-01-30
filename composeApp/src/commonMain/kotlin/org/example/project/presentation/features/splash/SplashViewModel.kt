package org.example.project.presentation.features.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.example.project.domain.models.Status
import org.example.project.domain.repository.RoomServiceRepository
import org.example.project.domain.result.onError
import org.example.project.domain.result.onSuccess

class SplashViewModel(private val roomServiceRepository: RoomServiceRepository):ViewModel() {

    private val _events = MutableSharedFlow<SplashEvents>()
    val events = _events.asSharedFlow()

    fun onIntent(intent: SplashIntent) {
        when (intent) {
            is SplashIntent.LoadRoom -> {
                loadRoom(intent.reconnectToken)
            }

        }
    }

    private fun loadRoom(reconnectToken:String){
        viewModelScope.launch {
            val result = roomServiceRepository.reconnectRoom(reconnectToken)
            result.onSuccess {
                if(it.room.status == Status.IN_GAME){
                    _events.emit(SplashEvents.NavigatePlayRoom(it.reconnectToken))
                }
                else{
                    _events.emit(SplashEvents.NavigateWaitingRoom(it.reconnectToken))
                }
            }.onError {
                _events.emit(SplashEvents.NavigateHome)
            }
        }
    }

}