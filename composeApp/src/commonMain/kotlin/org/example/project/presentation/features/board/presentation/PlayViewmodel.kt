package org.example.project.presentation.features.board.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toOffset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.components.Card
import org.example.project.domain.repository.RoomServiceRepository
import org.example.project.presentation.features.board.getTargetSlot

class PlayViewmodel(private val roomServiceRepository: RoomServiceRepository):ViewModel() {


    private val _uiState = MutableStateFlow(PlayBoardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                handCard = Card.entries.subList(0,13),
                centerTable = mapOf(
                    Seat.BOTTOM to StaticCard(
                        card = Card.entries[0],
                        seat = Seat.BOTTOM,
                        pos = IntOffset(700,270).toOffset(),
                        zIndex = 3f
                    ),
                    Seat.LEFT to StaticCard(
                        card = Card.entries[0],
                        seat = Seat.BOTTOM,
                        pos = IntOffset(700,270).toOffset(),
                        zIndex = 1f
                    ),
                    Seat.TOP to StaticCard(
                        card = Card.entries[0],
                        seat = Seat.BOTTOM,
                        pos = IntOffset(700,270).toOffset(),
                        zIndex = 2f
                    ),
                    Seat.RIGHT to StaticCard(
                        card = Card.entries[0],
                        seat = Seat.BOTTOM,
                        pos = IntOffset(700,270).toOffset(),
                        zIndex = 4f
                    )
                )
            )
        }

    }


    fun onIntent(intent: org.example.project.presentation.features.board.presentation.PlayBoardIntent) {
        viewModelScope.launch {

            when (intent) {
                is org.example.project.presentation.features.board.presentation.PlayBoardIntent.CardPlayed -> {
                    flightCard(
                        card = intent.card,
                        seat = Seat.BOTTOM,
                        start = intent.offset,
                        isLocal = true
                    )
                }
                is org.example.project.presentation.features.board.presentation.PlayBoardIntent.FinalyzeCard -> {
                    finalizeLanding(intent.movingCard)
                }
                is PlayBoardIntent.UpdateTableCenter->{
                    _uiState.update { state->
                        state.copy(tableCenter = intent.offset)
                    }
                }
            }

        }


    }


    private fun flightCard(card: Card,seat: Seat,start:Offset,isLocal:Boolean){

        val endPos= org.example.project.presentation.features.board.getTargetSlot(
            center = _uiState.value.tableCenter,
            seat = seat
        )

        _uiState.update { state->
            state.copy(
                handCard = if (isLocal) state.handCard.filter { it.id != card.id } else state.handCard,
                flyingCards = state.flyingCards + MovingCard(
                    card = card,
                    seat = seat,
                    startPos = start,
                    isLocal = isLocal,
                    endPos = endPos,
                    zIndex = state.centerTable.size+1f
                )
            )
        }

    }

    private fun finalizeLanding(movingCard: MovingCard){
        val zIndex = _uiState.value.centerTable.size+1
        _uiState.update { state->
            state.copy(
                flyingCards = state.flyingCards.filter { it.card.id != movingCard.card.id},
                centerTable = state.centerTable + (movingCard.seat to StaticCard(
                    card = movingCard.card,
                    seat = movingCard.seat,
                    pos = movingCard.endPos,
                    zIndex = zIndex.toFloat()
                ))
            )

        }
    }


}

