package org.example.project.di

import org.example.project.data.local.PrefrenceManager
import org.koin.core.module.Module
import org.koin.dsl.module

fun coreModule():Module = module{
    single { PrefrenceManager() }
}