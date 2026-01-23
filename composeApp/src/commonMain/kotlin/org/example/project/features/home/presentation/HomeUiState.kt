package org.example.project.features.entry.presentation

import org.example.project.components.Avatar

data class HomeUiState(
    val isLoading:Boolean=false,
    val nickName:String="",
    val avatar:Avatar,
    val nickNameFieldError:String?=null,
    val roomId:String=""
)