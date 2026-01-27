package org.example.project.presentation.features.board.presentation

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toOffset
import org.example.project.components.Card
import org.example.project.domain.models.Room
import org.koin.core.definition.IndexKey

data class PlayBoardUiState(
    val handCard:List<Card> = emptyList(),
    val centerTable:Map<Seat,StaticCard> = emptyMap(),
    val flyingCards:List<MovingCard> = emptyList(),
    val tableCenter: Offset = IntOffset(700,270).toOffset(),
    val playerSeats: Map<String, Seat> = emptyMap(),
    val seatPositions: Map<Seat, Offset> = emptyMap(),
    val localPlayerId:String = "",
    val room: Room? = null
)

data class MovingCard(
    val card: Card,
    val seat: Seat,
    val startPos:Offset,
    val endPos:Offset,
    val isLocal:Boolean,
    val zIndex:Float=1f
)

data class StaticCard(
    val card:Card,
    val seat: Seat,
    val pos:Offset,
    val zIndex: Float
)

enum class Seat { BOTTOM, LEFT, TOP, RIGHT }