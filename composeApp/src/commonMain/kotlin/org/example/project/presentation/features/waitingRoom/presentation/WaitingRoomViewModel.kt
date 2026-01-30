package org.example.project.presentation.features.waitingRoom.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.decodeFromJsonElement
import org.example.project.presentation.utils.UiSound
import org.example.project.domain.result.DataError
import org.example.project.domain.result.onError
import org.example.project.domain.result.onSuccess
import org.example.project.domain.models.MessageType
import org.example.project.domain.models.Room
import org.example.project.data.remote.socket.SocketEngine.Companion.json
import org.example.project.domain.models.Status
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
                    reconnectRoom(intent.reconnectId)


                }
                viewModelScope.launch {
                        val arr = intent.reconnectId.split(":")
                        println("Room id from reconnectId: ${arr[0]}")
                        gameSocketRepository.connect(arr[0])
                        observe()

                }
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
                            delay(1000)
                            _uiState.value.room?.players?.let {
                                if(it.size==4 && it[0].ready && it[1].ready && it[2].ready&& it[3].ready){
                                    _events.emit(WaitingRoomEvents.NavigateToPlayRoom)
                                }
                            }
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
                if(_uiState.value.room?.status == Status.IN_GAME){
                    _events.emit(WaitingRoomEvents.NavigateToPlayRoom)
                }
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
    private fun reconnectRoom(reconnectToken:String){

        viewModelScope.launch {
            _uiState.value=_uiState.value.copy(isLoading = true)
            val result = roomServiceRepository.reconnectRoom(reconnectToken)
            result.onSuccess {
                println(it)
                _uiState.value = _uiState.value.copy(room = it.room)
                if(_uiState.value.room?.status == Status.IN_GAME){
                    _events.emit(WaitingRoomEvents.NavigateToPlayRoom)
                }
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
                _events.emit(WaitingRoomEvents.NavigateBack)
            }
            _uiState.value=_uiState.value.copy(isLoading = false)
        }
    }






}