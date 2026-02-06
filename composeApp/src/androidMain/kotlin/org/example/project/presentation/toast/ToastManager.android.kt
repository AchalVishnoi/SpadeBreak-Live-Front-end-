package org.example.project.presentation.toast

import android.content.Context
import android.widget.Toast

actual class ToastManager(private val context: Context) {
    actual fun showToast(message: String, toastLength: ToastLength) {
        val duration = when (toastLength) {
            ToastLength.SHORT -> Toast.LENGTH_SHORT
            ToastLength.LONG -> Toast.LENGTH_LONG
        }
        Toast.makeText(context, message, duration).show()
    }
}