package org.example.project.features.board.presentation

import org.example.project.components.Card

sealed class PlayBoardIntent {
    data class CardPlayed(val card: Card, val player: Player) : PlayBoardIntent()
}