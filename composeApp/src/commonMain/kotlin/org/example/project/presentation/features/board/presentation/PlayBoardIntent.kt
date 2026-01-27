package org.example.project.presentation.features.board.presentation

import androidx.compose.ui.geometry.Offset
import org.example.project.components.Card

sealed class PlayBoardIntent {
    data class CardPlayed(val card: Card, val playerId:String, val offset: Offset) :PlayBoardIntent()
    data class FinalyzeCard(val movingCard: MovingCard):PlayBoardIntent()
    data class UpdateTableCenter(val offset: Offset):PlayBoardIntent()
}