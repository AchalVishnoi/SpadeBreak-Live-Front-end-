package org.example.project.presentation.features.board.presentation

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toOffset
import org.example.project.components.Card
import org.example.project.domain.models.Room
import org.koin.core.definition.IndexKey

data class PlayBoardUiState(
    val isLoading:Boolean = false,
    val reconnectToken:String = "",
    val handCard:List<Card> = emptyList(),
    val centerTable:Map<Seat,StaticCard> = emptyMap(),
    val flyingCards:List<MovingCard> = emptyList(),
    val tableCenter: Offset = IntOffset(700,270).toOffset(),
    val playerSeats: Map<String,Seat> = emptyMap(),
    val seatPositions: Map<Seat, Offset> = mapOf(
        Seat.RIGHT to Offset.Zero,
        Seat.LEFT to Offset.Zero,
        Seat.TOP to Offset.Zero,
        Seat.BOTTOM to Offset.Zero
    ),
    val localPlayerId:String = "",

    val room: Room? = null,

    val showScoreCard:Boolean = false,
    val gameCompleted:Boolean = false
)

data class MovingCard(
    val card: Card,
    val seat: Seat,
    val startPos:Offset,
    val endPos:Offset,
    val currRotation:Float=0f,
    val targetRotation:Float=0f,
    val isLocal:Boolean,
    val zIndex:Float=1f,
    val toCenter:Boolean=true
)

data class StaticCard(
    val card:Card,
    val seat: Seat,
    val pos:Offset,
    val zIndex: Float
)

enum class Seat { BOTTOM, LEFT, TOP, RIGHT }