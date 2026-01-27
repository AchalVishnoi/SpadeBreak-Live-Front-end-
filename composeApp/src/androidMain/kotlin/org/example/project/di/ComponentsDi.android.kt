package org.example.project.di

import org.example.project.presentation.SoundPlayer
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun soundPlayerModule(): Module = module {
    single { SoundPlayer(get()) }
}