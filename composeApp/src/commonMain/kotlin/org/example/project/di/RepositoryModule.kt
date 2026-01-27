package org.example.project.di

import org.example.project.data.repositoryImpl.GameSocketRepositoryImpl
import org.example.project.data.repositoryImpl.RoomServiceRepositoryImpl
import org.example.project.domain.repository.GameSocketRepository
import org.example.project.domain.repository.RoomServiceRepository
import org.koin.core.module.Module
import org.koin.dsl.module

fun repositoryModule():Module= module {
    single<RoomServiceRepository> { RoomServiceRepositoryImpl(get()) }
    single<GameSocketRepository> { GameSocketRepositoryImpl(get()) }
}
