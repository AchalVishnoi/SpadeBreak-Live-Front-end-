package org.example.project.di

import org.example.project.serverRoom.rest.ApiEngine
import org.example.project.serverRoom.socket.SocketEngine
import org.example.project.serverRoom.rest.provideKtorClient
import org.example.project.serverRoom.socket.GameSocketRepository
import org.koin.core.module.Module
import org.koin.dsl.module

fun networkModule():Module= module{
    single { provideKtorClient() }
    single { ApiEngine(get()) }
    single { SocketEngine(get()) }
    single { GameSocketRepository(get()) }
}