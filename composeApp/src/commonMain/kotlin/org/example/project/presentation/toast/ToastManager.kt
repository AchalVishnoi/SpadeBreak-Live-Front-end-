package org.example.project.presentation.toast

expect class ToastManager {
    fun showToast(message: String,toastLength: ToastLength = ToastLength.SHORT)
}