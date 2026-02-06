package org.example.project

import androidx.compose.runtime.staticCompositionLocalOf
import org.example.project.presentation.utils.SoundPlayer

val LocalSoundPlayer = staticCompositionLocalOf<SoundPlayer> {
    error("No SoundPlayer provided")
}


