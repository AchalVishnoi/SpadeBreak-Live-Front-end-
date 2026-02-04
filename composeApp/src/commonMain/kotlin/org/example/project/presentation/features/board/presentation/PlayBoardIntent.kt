package org.example.project.presentation.features.board.presentation

import androidx.compose.ui.geometry.Offset
import org.example.project.components.Card

sealed class PlayBoardIntent {
    data class CardPlayed(val card: Card, val playerId:String, val offset: Offset) :PlayBoardIntent()
    data class ReconnectRoom(val reconnectToken:String):PlayBoardIntent()
    data class FinalyzeCard(val movingCard: MovingCard):PlayBoardIntent()
    data class UpdateTableCenter(val offset: Offset):PlayBoardIntent()
    data class UpdateSeatPosition(val seat: Seat,val offset: Offset):PlayBoardIntent()
    data object ShowScoreCard:PlayBoardIntent()
    data object HideScoreCard:PlayBoardIntent()
    data object TrickWinner:PlayBoardIntent()
}