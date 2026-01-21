package org.example.project.features.board.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.components.Card

class PlayViewmodel():ViewModel() {


    private val _uiState = MutableStateFlow(PlayBoardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        dealDummyHands()
    }

    fun onIntent(intent: PlayBoardIntent) {
        viewModelScope.launch {

            when (intent) {
                is PlayBoardIntent.CardPlayed -> {
                    delay(300)
                    updatePlayerHand(
                        playerId = intent.player.playerId,
                        card = intent.card
                    )
                    _uiState.value = _uiState.value.copy(

                        centerPlayedCards = _uiState.value.centerPlayedCards + PlayedCard(
                            player = intent.player,
                            card = intent.card
                        )
                    )
                }
            }

        }


    }

    private fun dealDummyHands() {
        val deck = Card.entries.shuffled()

        val p1 = deck.subList(0, 13).sortedBy { it.suit.ordinal * 20 + it.rank.value }
        val p2 = deck.subList(13, 26).sortedBy { it.suit.ordinal * 20 + it.rank.value }
        val p3 = deck.subList(26, 39).sortedBy { it.suit.ordinal * 20 + it.rank.value }
        val p4 = deck.subList(39, 52).sortedBy { it.suit.ordinal * 20 + it.rank.value }

        _uiState.value = PlayBoardUiState(
            player1 = Player("1", 1, p1),
            player2 = Player("2", 2, p2),
            player3 = Player("3", 3, p3),
            player4 = Player("4", 4, p4),
            currentPlayer = "1",
            centerPlayedCards = emptyList()
        )
    }

    private fun updatePlayerHand(
        playerId: String,
        card: Card
    ) {

        println("Played card ${card.id} ${card.suit}")
        fun removeCardFromHand(player: Player): Player {

            println(playerId==player.playerId+" "+playerId.equals(player.playerId))

            return if (player.playerId == playerId) {
                println("Player$playerId card removed: ${player.handCards.size}")

                player.copy(
                    handCards = player.handCards-card
                )
            } else player
        }


        _uiState.value = _uiState.value.copy(
            player1 = removeCardFromHand(_uiState.value.player1),
            player2 = removeCardFromHand(_uiState.value.player2),
            player3 = removeCardFromHand(_uiState.value.player3),
            player4 = removeCardFromHand(_uiState.value.player4),
            currentPlayer = playerId
        )

        println("Player1: ${_uiState.value.player1.handCards.size}")
        println("Player2: ${_uiState.value.player2.handCards.size}")
        println("Player3: ${_uiState.value.player3.handCards.size}")
        println("Player4: ${_uiState.value.player4.handCards.size}")
    }

}