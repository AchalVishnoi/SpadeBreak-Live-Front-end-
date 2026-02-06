package org.example.project.presentation.utils

expect class SoundPlayer{
    fun playSound(sound: UiSound)
}

enum class UiSound {
    CLICK,
    SUCCESS,
    ERROR,
    CARD_SHUFFLE,
    GAME_WIN,
    GAME_LOSE,
    WATER_DROP,
    READY_SOUND
}