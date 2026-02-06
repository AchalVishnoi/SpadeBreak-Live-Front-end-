package org.example.project.di

import org.koin.core.module.Module
import org.koin.dsl.module

fun appModule():List<Module> = listOf(
    featuresModel(),
    soundPlayerModule(),
    networkModule(),
    coreModule(),
    repositoryModule(),
    toastModule()
)