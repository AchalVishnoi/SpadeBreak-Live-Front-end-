package org.example.project.presentation.features.board.presentation

import org.example.project.components.Card
import org.example.project.domain.models.Player

sealed class PlayBoardEvents {
    data class CardPlayed(val card: Card, val player: Player) : PlayBoardEvents()
}