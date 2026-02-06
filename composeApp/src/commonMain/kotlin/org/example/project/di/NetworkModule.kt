package org.example.project.di

import org.example.project.data.remote.rest.ApiEngine
import org.example.project.data.remote.socket.SocketEngine
import org.example.project.data.remote.rest.provideKtorClient
import org.example.project.data.repositoryImpl.GameSocketRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

fun networkModule():Module= module{
    single { provideKtorClient() }
    single { ApiEngine(get()) }
    single { SocketEngine(get()) }
    single { GameSocketRepositoryImpl(get()) }
}