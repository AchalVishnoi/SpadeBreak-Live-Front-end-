package org.example.project.di

import org.example.project.serverRoom.ApiEngine
import org.example.project.serverRoom.SocketEngine
import org.example.project.serverRoom.provideKtorClient
import org.koin.core.module.Module
import org.koin.dsl.module

fun networkModule():Module= module{
    single { provideKtorClient() }
    single { ApiEngine(get()) }
    single { SocketEngine(get()) }
}