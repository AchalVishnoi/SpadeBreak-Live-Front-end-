package org.example.project.presentation.features.waitingRoom.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.decodeFromJsonElement
import org.example.project.presentation.UiSound
import org.example.project.domain.result.DataError
import org.example.project.domain.result.onError
import org.example.project.domain.result.onSuccess
import org.example.project.domain.models.MessageType
import org.example.project.domain.models.Room
import org.example.project.data.repositoryImpl.GameSocketRepositoryImpl
import org.example.project.data.remote.socket.SocketEngine.Companion.json
import org.example.project.domain.repository.GameSocketRepository
import org.example.project.domain.repository.RoomServiceRepository

class WaitingRoomViewModel(private val gameSocketRepository: GameSocketRepository, private val roomServiceRepository: RoomServiceRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(WaitingRoomUiState())
    val uiState = _uiState.asStateFlow()

    private val _events= MutableSharedFlow<WaitingRoomEvents>()
    val events = _events.asSharedFlow()

    private val socketEvents = gameSocketRepository.event

    fun onIntent(intent: WaitingRoomIntent){
        when(intent){
            is WaitingRoomIntent.Connect -> {
                viewModelScope.launch {
                    gameSocketRepository.connect(intent.roomId)
                    observe()
                }
                getRoom(intent.roomId)

            }
            is WaitingRoomIntent.Ready   -> {}
        }
    }

    private fun observe(){
        viewModelScope.launch {
            gameSocketRepository.event.collect{
                when(it.type){
                     MessageType.PLAYER_JOINED->{
                         val room = json.decodeFromJsonElement<Room>(it.playLoad!!)
                         _uiState.value=_uiState.value.copy(room = room)
                         _events.emit(WaitingRoomEvents.ShowToast("${room.players.last().nickname} joined!"))
                         viewModelScope.launch {
                             _events.emit(WaitingRoomEvents.PlaySound(UiSound.WATER_DROP))
                         }
                     }
                    MessageType.PLAYER_LEFT->{
                        val room = json.decodeFromJsonElement<Room>(it.playLoad!!)
                        _uiState.value=_uiState.value.copy(room = room)
                        _events.emit(WaitingRoomEvents.ShowToast("${room.players.last().nickname} left!"))
                    }
                    MessageType.PLAYER_IS_READY->{
                        val room = json.decodeFromJsonElement<Room>(it.playLoad!!)
                        _uiState.value=_uiState.value.copy(room = room)
                        viewModelScope.launch {
                            _events.emit(WaitingRoomEvents.PlaySound(UiSound.READY_SOUND))
                        }
                    }
                    else->{}
                }
            }
        }
    }

    private fun getRoom(roomId:String){

        viewModelScope.launch {
            _uiState.value=_uiState.value.copy(isLoading = true)
            val result = roomServiceRepository.getRoom(roomId)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(room = it)
            }.onError {
                when(it){
                    is DataError.Remote.ServerMessage ->{ _events.emit(WaitingRoomEvents.ShowToast(it.message?:"field to process request, try again!!"))}
                    DataError.Remote.NOT_FOUND -> _events.emit(WaitingRoomEvents.ShowToast("room not found"))
                    DataError.Remote.SERVER_ERROR -> _events.emit(WaitingRoomEvents.ShowToast("server error, try again later"))
                    DataError.Remote.NO_INTERNET -> _events.emit(WaitingRoomEvents.ShowToast("Check your internet connection"))
                    DataError.Remote.TIMEOUT_ERROR -> _events.emit(WaitingRoomEvents.ShowToast("Request timed out"))
                    DataError.Remote.UNKNOWN -> _events.emit(WaitingRoomEvents.ShowToast("Something went wrong"))
                    DataError.Remote.REDIRECT_ERROR -> _events.emit(WaitingRoomEvents.ShowToast("Unexpected redirect"))
                    else -> _events.emit(WaitingRoomEvents.ShowToast("Pata nahi kya ghatna ghati"))
                }
            }
            _uiState.value=_uiState.value.copy(isLoading = false)
        }
    }






}