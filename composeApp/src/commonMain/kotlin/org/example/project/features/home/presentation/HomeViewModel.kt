package org.example.project.features.entry.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.project.components.Avatar
import org.example.project.features.home.presentation.HomeIntent

class HomeViewModel:ViewModel() {


    private val _uiState = MutableStateFlow(HomeUiState(
        nickName = "",
        avatar = Avatar.entries.random()
    ))

    val uiState = _uiState.asStateFlow()

    fun onIntent(intent: HomeIntent){
        when(intent){
            is HomeIntent.AvatarChanged -> {
                _uiState.value = _uiState.value.copy(avatar = intent.avatar)
            }
            is HomeIntent.NickNameChanged->{
                _uiState.value = _uiState.value.copy(nickName = intent.nickName)
            }
            is HomeIntent.CreateRoomClicked->{

            }
            is HomeIntent.JoinRoomClicked->{

            }
            else -> {}
        }
    }

    private fun createRoom(){

    }



}