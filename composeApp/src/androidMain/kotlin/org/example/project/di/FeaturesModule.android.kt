package org.example.project.di

import org.example.project.presentation.toast.ToastManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun toastModule(): Module = module {
    single { ToastManager(get()) }
}