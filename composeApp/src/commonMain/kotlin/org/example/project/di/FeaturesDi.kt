package org.example.project.di

import org.example.project.features.board.presentation.PlayViewmodel
import org.example.project.features.entry.presentation.HomeViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

fun playingBoardModel(): Module = module {
    factory { PlayViewmodel() }
}

fun homeModel():Module = module {
    factory { HomeViewModel() }
}