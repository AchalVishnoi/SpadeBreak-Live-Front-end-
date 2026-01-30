package org.example.project.presentation.features.board.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toOffset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.decodeFromJsonElement
import org.example.project.components.Card
import org.example.project.data.remote.socket.SocketEngine.Companion.json
import org.example.project.domain.models.MessageType
import org.example.project.domain.models.Player
import org.example.project.domain.models.PlayerRoundScore
import org.example.project.domain.models.Room
import org.example.project.domain.models.Status
import org.example.project.domain.repository.GameSocketRepository
import org.example.project.domain.repository.RoomServiceRepository
import org.example.project.domain.result.DataError
import org.example.project.domain.result.onError
import org.example.project.domain.result.onSuccess
import org.example.project.presentation.features.board.getTargetSlot
import org.example.project.presentation.features.waitingRoom.presentation.WaitingRoomEvents
import org.example.project.presentation.utils.UiSound

class PlayViewmodel(private val roomServiceRepository: RoomServiceRepository,private val gameSocketRepository: GameSocketRepository):ViewModel() {


    private val _uiState = MutableStateFlow(PlayBoardUiState())
    val uiState = _uiState.asStateFlow()

    private val _events= MutableSharedFlow<PlayBoardEvents>()
    val events=_events.asSharedFlow()



    fun onIntent(intent: PlayBoardIntent) {
        viewModelScope.launch {

            when (intent) {
                is PlayBoardIntent.ReconnectRoom->{
                   reconnectRoom(intent.reconnectToken)

                    viewModelScope.launch{
                        gameSocketRepository.connect(intent.reconnectToken.split(":")[0])
                        observe()
                    }

                }
                is PlayBoardIntent.CardPlayed -> {
                    flightCard(
                        card = intent.card,
                        seat = Seat.BOTTOM,
                        start = intent.offset,
                        isLocal = true
                    )
                }
                is PlayBoardIntent.FinalyzeCard -> {
                    finalizeLanding(intent.movingCard)
                }
                is PlayBoardIntent.UpdateTableCenter->{
                    _uiState.update { state->
                        state.copy(tableCenter = intent.offset)
                    }
                }

                is PlayBoardIntent.UpdateSeatPosition->{
                    _uiState.update {
                        it.copy(
                            seatPositions = it.seatPositions + (intent.seat to intent.offset)
                        )
                    }
                }

            }

        }


    }


    private fun flightCard(card: Card,seat: Seat,start:Offset,isLocal:Boolean){

        println("flight card called with seat: $seat and card: ${card.id}")

        val endPos=getTargetSlot(
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

    private fun reconnectRoom(reconnectToken:String){

        viewModelScope.launch {
            _uiState.value=_uiState.value.copy(isLoading = true)
            val result = roomServiceRepository.reconnectRoom(reconnectToken)
            result.onSuccess {result->
                _uiState.update {
                    it.copy(
                        room = result.room,
                        localPlayerId = result.playerId
                    )
                }
                mapPlayerIdsWithSeat(result.room.players,result.playerId)
                result.room.game?.roundState?.handCards?.let {
                    it[result.playerId]?.let { it1 -> updateHandCards(it1) }
                }


            }.onError{
                when(it){
                    is DataError.Remote.ServerMessage ->{ _events.emit(PlayBoardEvents.ShowToast(it.message?:"field to process request, try again!!"))}
                    DataError.Remote.NOT_FOUND -> _events.emit(PlayBoardEvents.ShowToast("room not found"))
                    DataError.Remote.SERVER_ERROR -> _events.emit(PlayBoardEvents.ShowToast("server error, try again later"))
                    DataError.Remote.NO_INTERNET -> _events.emit(PlayBoardEvents.ShowToast("Check your internet connection"))
                    DataError.Remote.TIMEOUT_ERROR -> _events.emit(PlayBoardEvents.ShowToast("Request timed out"))
                    DataError.Remote.UNKNOWN -> _events.emit(PlayBoardEvents.ShowToast("Something went wrong"))
                    DataError.Remote.REDIRECT_ERROR -> _events.emit(PlayBoardEvents.ShowToast("Unexpected redirect"))
                    else -> _events.emit(PlayBoardEvents.ShowToast("Pata nahi kya ghatna ghati"))
                }
            }
            _uiState.value=_uiState.value.copy(isLoading = false)
        }
    }

    private fun updateHandCards(handCards:List<String>){
       val handCard = handCards.map {
           Card.entries.find { card->
               card.id==it
               }?:Card.entries[0]
       }
        _uiState.update {
            it.copy(
                handCard = handCard
            )
        }
    }


    private fun observe(){
        viewModelScope.launch {
            gameSocketRepository.event.collect{
                when(it.type){

                    MessageType.NEW_ROUND_STARTED->{
                        val room = json.decodeFromJsonElement<Room>(it.playLoad!!)
                        room.game?.roundState?.handCards?.get(_uiState.value.localPlayerId)?.let {
                            it1 -> updateHandCards(it1)
                        }
                        _uiState.update { state->
                            state.copy(
                                room = room
                            )
                        }
                    }
                    MessageType.BET->{
                        val room = json.decodeFromJsonElement<Room>(it.playLoad!!)
                        _uiState.update { state->
                            state.copy(
                                room = room
                            )
                        }
                    }
                    MessageType.GAME_SCORE_UPDATED->{
                        val room = json.decodeFromJsonElement<Room>(it.playLoad!!)
                        _uiState.update { state->
                            state.copy(
                                room = room
                            )
                        }
                        _events.emit(PlayBoardEvents.ShowScoreCard)
                    }
                    MessageType.PLAYER_LEFT->{
                        val room = json.decodeFromJsonElement<Room>(it.playLoad!!)
                        _uiState.update { state->
                            state.copy(
                                room = room
                            )
                        }
                        _events.emit(PlayBoardEvents.ShowToast(
                            room.players.filter { it1->
                                it1.id==it.playerId
                            }.get(0).nickname +
                                " left!"))
                        _events.emit(PlayBoardEvents.NavigateBack)
                    }
                    MessageType.ROUND_SCORE_UPDATED->{
                        val room = json.decodeFromJsonElement<Room>(it.playLoad!!)
                        room.game?.roundState?.score?.let {
                            val winner=decideWinner(it)
                        }


                    }
                    MessageType.PLAY_CARD->{
                        val room = json.decodeFromJsonElement<Room>(it.playLoad!!)
                        println("play card called in viewmodel")
                        _uiState.update { state->
                            state.copy(
                                room = room
                            )
                        }
                        val seat = _uiState.value.playerSeats[it.playerId]
                        if(it.playerId!=_uiState.value.localPlayerId){
                            flightCard(
                                card = Card.getCardById(room.game?.roundState?.centerTrickedCard?.get(it.playerId)!!),
                                seat = seat!!,
                                start = _uiState.value.seatPositions.get(seat)!!,
                                isLocal = true
                            )
                        }
                    }

                    else->{}
                }
            }
        }
    }


    private fun decideWinner(updatedScore:Map<String,PlayerRoundScore>):String{
        println("dicide winner called")
        val currScore=_uiState.value.room?.game?.roundState?.score!!
        var winner=""
        updatedScore.forEach {
            if(it.value.currPoints > currScore[it.key]?.currPoints!! ){
                winner=it.key
            }
        }

        println("dicided winner: $winner")

        return winner
    }

    fun findSeatByIndex(localPlayerIndex:Int,playerIdx:Int){

    }
    private fun mapPlayerIdsWithSeat(players:List<Player>,localPlayerId:String){

        var playersIds:List<String> = emptyList()
        players.forEach {
            playersIds=playersIds + it.id
        }

        val k = playersIds.indexOf(localPlayerId)
        var seatMap:Map<String,Seat> = emptyMap()
        seatMap = mapOf(
            playersIds[(k+0)%4] to Seat.BOTTOM,
            playersIds[(k+1)%4] to Seat.RIGHT,
            playersIds[(k+2)%4] to Seat.TOP,
            playersIds[(k+3)%4] to Seat.LEFT
        )

        _uiState.update {
            it.copy(
                playerSeats = seatMap
            )
        }

    }
}

