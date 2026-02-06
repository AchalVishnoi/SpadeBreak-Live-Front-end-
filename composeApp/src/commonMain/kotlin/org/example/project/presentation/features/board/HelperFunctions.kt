package org.example.project.presentation.features.board

import org.example.project.components.Card
import org.example.project.components.Suit
import org.example.project.domain.models.PlayerRoundScore
import org.example.project.domain.models.RoundStatus
import org.example.project.domain.models.Status
import org.example.project.presentation.features.board.presentation.PlayBoardUiState
import org.example.project.presentation.features.board.presentation.Seat

object HelperFunctions {
    fun canPlay(card: Card, uiState: PlayBoardUiState): Boolean {

        val room = uiState.room ?: return false
        val round = room.game?.roundState ?: return false

        if (round.playerTurn != uiState.localPlayerId||round.status == RoundStatus.BETTING) return false

        val tableCards = uiState.centerTable.values
        val hand = uiState.handCard

        if (tableCards.isEmpty()) return true

        val leadSuit = getLeadSuit(uiState) ?: return true

        val hasLeadSuit = hand.any { it.suit == leadSuit }

        if (hasLeadSuit && card.suit != leadSuit) return false

        val highestOnTable = tableCards
            .filter { it.card.suit == leadSuit }
            .maxByOrNull { it.card.rank.value }

        if (highestOnTable == null) return true

        val hasHigher = hand.any {
            it.suit == leadSuit && it.rank.value > highestOnTable.card.rank.value
        }

        return if (hasHigher) {
            card.suit == leadSuit && card.rank.value > highestOnTable.card.rank.value
        } else {
            true
        }
    }


    fun haveSuite(_uiState: PlayBoardUiState,leaderSuit: Suit):Boolean{

        val trickLeader = _uiState.room?.game?.roundState?.trickLeaderId

        _uiState.handCard.forEach {
            if(it.suit==leaderSuit){
                return true
            }
        }

        return false
    }

    fun haveSpades(_uiState: PlayBoardUiState):Boolean{
        _uiState.handCard.forEach {
            if(it.suit==Suit.SPADES){
                return true
            }
        }
        return false
    }

    fun getLeadSuit(uiState: PlayBoardUiState): Suit? {
        val round = uiState.room?.game?.roundState ?: return null
        val trickLeaderId = round.trickLeaderId ?: return null

        val leaderCardId = round.centerTrickedCard[trickLeaderId] ?: return null

        return Card.getCardById(leaderCardId).suit
    }

    fun List<Card>.hasSuit(suit: Suit): Boolean =
        this.any { it.suit == suit }

    fun calculateFinalScore(score:PlayerRoundScore?):Double{
        if(score==null) return  0.0
        val bet=score.bet
        val score= score.currPoints
        return if(bet>score) -1.0*bet
        else{
            bet*1.0+(score*1.0-bet)/10
        }
    }



}