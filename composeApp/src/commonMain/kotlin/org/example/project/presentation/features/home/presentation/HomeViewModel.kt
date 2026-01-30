package org.example.project.features.entry.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.components.Avatar
import org.example.project.domain.repository.RoomServiceRepository
import org.example.project.domain.result.DataError
import org.example.project.domain.result.onError
import org.example.project.domain.result.onSuccess
import org.example.project.presentation.features.home.presentation.HomeEvents
import org.example.project.presentation.features.home.presentation.HomeIntent

class HomeViewModel(private val repo: RoomServiceRepository):ViewModel() {


    private val _uiState = MutableStateFlow(HomeUiState(
        nickName = "",
        avatar = Avatar.entries.random()
    ))

    val uiState = _uiState.asStateFlow()

    private val _events= MutableSharedFlow<HomeEvents>()
    val events = _events.asSharedFlow()

    fun onIntent(intent: HomeIntent){
        when(intent){
            is HomeIntent.AvatarChanged -> {
                _uiState.value = _uiState.value.copy(avatar = intent.avatar)
            }
            is HomeIntent.NickNameChanged->{
                _uiState.value = _uiState.value.copy(nickName = intent.nickName)
            }
            is HomeIntent.CreateRoomClicked->{
                createRoom()
            }
            is HomeIntent.JoinRoomClicked ->{
                joinRoom()
            }
            is HomeIntent.RoomIdChanged->{
                _uiState.value = _uiState.value.copy(enterdRoomId = intent.roomId)
            }
            is HomeIntent.CancelClicked->{
                _uiState.value = _uiState.value.copy(enterdRoomId = "")
            }
            else -> {}
        }
    }

    private fun createRoom(){

        if(_uiState.value.nickName.isBlank()||_uiState.value.nickName.length<2){
            _uiState.value = _uiState.value.copy(nickNameFieldError = "Invalid Nick Name")
            return
        }


        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = repo.createRoom(_uiState.value.nickName,_uiState.value.avatar.name)
            println(result)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(room = it.room)
                _events.emit(HomeEvents.NavigateToWaitingRoom(it.room,it.playerId,it.reconnectToken))
                println("room id:"+it.room.id)
            }.onError {
                when(it){
                    is DataError.Remote.ServerMessage ->{ _events.emit(HomeEvents.ShowToast(it.message?:"field to process request, try again!!"))}
                    else -> _events.emit(HomeEvents.ShowToast("field to process request, try again!!"))
                }
            }

            _uiState.value = _uiState.value.copy(isLoading = false)
        }





    }

    private fun joinRoom(){

        if(_uiState.value.nickName.isBlank()||_uiState.value.nickName.length<2){
            _uiState.value = _uiState.value.copy(nickNameFieldError = "Invalid Nick Name")
            return
        }

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = repo.joinRoom(
                _uiState.value.nickName,
                _uiState.value.avatar.id,
                _uiState.value.enterdRoomId)
            println(result)
            result.onSuccess {
                _uiState.value = _uiState.value.copy(room = it.room)
                _events.emit(HomeEvents.NavigateToWaitingRoom(it.room,it.playerId,it.reconnectToken))
            }.onError {
                when(it){
                    is DataError.Remote.ServerMessage ->{ _events.emit(HomeEvents.ShowToast(it.message?:"field to process request, try again!!"))}
                    DataError.Remote.NOT_FOUND -> _events.emit(HomeEvents.ShowToast("room not found"))
                    DataError.Remote.SERVER_ERROR -> _events.emit(HomeEvents.ShowToast("server error, try again later"))
                    DataError.Remote.NO_INTERNET -> _events.emit(HomeEvents.ShowToast("Check your internet connection"))
                    DataError.Remote.TIMEOUT_ERROR -> _events.emit(HomeEvents.ShowToast("Request timed out"))
                    DataError.Remote.UNKNOWN -> _events.emit(HomeEvents.ShowToast("Something went wrong"))
                    DataError.Remote.REDIRECT_ERROR -> _events.emit(HomeEvents.ShowToast("Unexpected redirect"))
                    else -> _events.emit(HomeEvents.ShowToast("Pata nahi kya ghatna ghati"))
                }
            }

            _uiState.value = _uiState.value.copy(isLoading = false)

        }





    }

}



