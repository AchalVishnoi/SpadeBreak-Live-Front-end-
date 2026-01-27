package org.example.project.di

import org.example.project.features.entry.presentation.HomeViewModel
import org.example.project.presentation.features.board.presentation.PlayViewmodel
import org.example.project.presentation.features.waitingRoom.presentation.WaitingRoomViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

fun playingBoardModel(): Module = module {
    factory { PlayViewmodel(get()) }
}

fun homeModel():Module = module {
    factory { HomeViewModel(get()) }
    factory { WaitingRoomViewModel(get(),get()) }
}