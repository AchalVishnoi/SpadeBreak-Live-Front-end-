package org.example.project.features.board.presentation

import org.example.project.components.Card

data class PlayBoardUiState(

    val player1:Player = Player(
        playerId = "1",
        seat = 1,
        handCards = emptyList()

    ),
    val player2:Player = Player(
        playerId = "2",
        seat = 2,
        handCards = emptyList()

    ),
    val player3:Player = Player(
        playerId = "3",
        seat = 3,
        handCards = emptyList()

    ),
    val player4:Player = Player(
        playerId = "4",
        seat = 4,
        handCards = emptyList()

    ),


    val currentPlayer:String="1",

    val centerPlayedCards:List<PlayedCard> = emptyList(),

)

data class PlayedCard(
    val player: Player,
    val card: Card
)

data class Player(
    val playerId:String,
    val seat:Int,
    val handCards: List<Card>
)
