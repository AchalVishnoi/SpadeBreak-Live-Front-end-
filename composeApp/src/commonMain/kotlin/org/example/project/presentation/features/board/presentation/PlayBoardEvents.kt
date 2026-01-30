package org.example.project.presentation.features.board.presentation

import org.example.project.components.Card
import org.example.project.domain.models.Player

sealed class PlayBoardEvents {
    data class CardPlayed(val card: Card, val player: Player) : PlayBoardEvents()
    data class ShowToast(val message: String) : PlayBoardEvents()
    data object ShowScoreCard:PlayBoardEvents()
    data object NavigateBack:PlayBoardEvents()
    data class TrickWinner(val player: Player,val seat: Seat):PlayBoardEvents()
}


