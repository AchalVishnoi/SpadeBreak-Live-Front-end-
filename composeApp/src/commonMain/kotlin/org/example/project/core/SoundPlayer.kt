package org.example.project.core

expect class SoundPlayer{
    fun playSound(sound: UiSound)
}

enum class UiSound {
    CLICK,
    SUCCESS,
    ERROR,
    CARD_SHUFFLE,
    GAME_WIN,
    GAME_LOSE
}