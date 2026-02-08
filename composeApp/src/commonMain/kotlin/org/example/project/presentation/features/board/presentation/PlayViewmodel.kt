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
import org.example.project.components.Reaction
import org.example.project.data.remote.socket.SocketEngine.Companion.json
import org.example.project.domain.models.MessageType
import org.example.project.domain.models.Player
import org.example.project.domain.models.PlayerRoundScore
import org.example.project.domain.models.Room
import org.example.project.domain.models.Status
import org.example.project.domain.models.getPlayerById
import org.example.project.domain.repository.GameSocketRepository
import org.example.project.domain.repository.RoomServiceRepository
import org.example.project.domain.result.DataError
import org.example.project.domain.result.onError
import org.example.project.domain.result.onSuccess
import org.example.project.presentation.features.board.getRotationForSeat
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
                    playCard(intent.card)
                    flightCardToCenter(
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

                is PlayBoardIntent.ShowScoreCard->{
                    _uiState.update {
                        it.copy(
                            showScoreCard = true
                        )
                    }
                }

                is PlayBoardIntent.HideScoreCard->{
                    _uiState.update {
                        it.copy(
                            showScoreCard = false
                        )
                    }
                }

                is PlayBoardIntent.PlayBid->{
                    playBid(intent.bid)
                }

                is PlayBoardIntent.LeaveRoom->{
                    leaveRoom(intent.reconnectToken)
                }

                is PlayBoardIntent.ReactionMessage->{
                    react(intent.reaction)
                }

                else ->{}

            }

        }


    }


    private fun observe(){
        viewModelScope.launch {
            gameSocketRepository.event.collect{
                when(it.type){

                    MessageType.NEW_ROUND_STARTED->{
                        val room = json.decodeFromJsonElement<Room>(it.playLoad!!)

                        room.game?.roundState?.handCards?.let {
                            it.forEach {
                                if(it.key==_uiState.value.localPlayerId){
                                    updateHandCards(it.value)
                                }
                                else{
                                    val seat = _uiState.value.playerSeats[it.key]
                                    updatePlayersHandCards(seat!!,it.value)
                                }
                            }
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
                        _uiState.update {
                            it.copy(
                                showScoreCard = true
                            )
                        }
                        _events.emit(PlayBoardEvents.ShowScoreCard)
                    }
                    MessageType.PLAYER_LEFT->{
                        val room = json.decodeFromJsonElement<Room>(it.playLoad!!)
                        val name=_uiState.value.room?.players?.getPlayerById(it.playerId?:"1")
                        if(it.playerId==uiState.value.localPlayerId){
                            _events.emit(PlayBoardEvents.ShowToast("You left!"))
                        }
                        else{
                            _events.emit(PlayBoardEvents.ShowToast("${name?.nickname} left!"))
                            _events.emit(PlayBoardEvents.NavigateBack)
                        }
                    }
                    MessageType.ROUND_SCORE_UPDATED->{
                        val room = json.decodeFromJsonElement<Room>(it.playLoad!!)
                        room.game?.roundState?.score?.let {
                            val winner=decideWinner(it)
                            val seat = _uiState.value.playerSeats[winner]
                            flightTheCardsToWinner(seat?:Seat.BOTTOM)
                        }

                        delay(1500)

                        _uiState.update { state->
                            state.copy(
                                room = room,
                                cardPlayed = false
                            )
                        }

                    }
                    MessageType.PLAY_CARD->{
                        val room = json.decodeFromJsonElement<Room>(it.playLoad!!)
                        println("play card called in viewmodel")
                        val seat = _uiState.value.playerSeats[it.playerId]
                        if(it.playerId!=_uiState.value.localPlayerId){
                            updatePlayersHandCards(
                                seat!!,
                                room.game?.roundState?.handCards?.get(it.playerId)!!
                            )
                            flightCardToCenter(
                                card = Card.getCardById(room.game?.roundState?.centerTrickedCard?.get(it.playerId)!!),
                                seat = seat!!,
                                start = _uiState.value.seatPositions.get(seat)!!,
                                isLocal = true
                            )
                        }

                        if((room?.game?.roundState?.centerTrickedCard?.size ?: 0) < 4){
                            _uiState.update { state->
                                state.copy(
                                    room = room,
                                    cardPlayed = false
                                )
                            }
                        }




                    }

                    MessageType.GAME_COMPLETED ->{
                        val room = json.decodeFromJsonElement<Room>(it.playLoad!!)
                        _uiState.update {

                            it.copy(
                                room = room,
                                showScoreCard = true,
                                gameCompleted = true
                            )
                        }
                    }

                    MessageType.REACTION -> {

                        val reaction = json.decodeFromJsonElement<Reaction>(it.playLoad!!)
                        val seat = _uiState.value.playerSeats[it.playerId]

                        viewModelScope.launch {

                            val id=_uiState.value.reactionsList.size

                            _uiState.update {
                                it.copy(
                                    reactionsList = uiState.value.reactionsList + ReactionMessage(
                                        reaction = reaction,
                                        seat = seat?:Seat.BOTTOM,
                                        id=id
                                    )
                                )
                            }

                            delay(5000)

                            _uiState.update {
                                it.copy(
                                    reactionsList = uiState.value.reactionsList.filter {
                                        it.id!=id && it.seat != seat && it.reaction!=reaction
                                    }
                                )
                            }

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

    private fun flightCardToCenter(card: Card,seat: Seat,start:Offset,isLocal:Boolean){

        println("flight card called with seat: $seat and card: ${card.id}")

        val endPos=getTargetSlot(
            center = _uiState.value.tableCenter,
            seat = seat
        )

        val rotation = getRotationForSeat(seat)

        _uiState.update { state->
            state.copy(
                handCard = if (isLocal) state.handCard.filter { it.id != card.id } else state.handCard,
                flyingCards = state.flyingCards + MovingCard(
                    card = card,
                    seat = seat,
                    startPos = start,
                    endPos = endPos,
                    isLocal = isLocal,
                    targetRotation = rotation,
                    currRotation = rotation,
                    zIndex = state.centerTable.size*1f+1f
                )
            )
        }

        viewModelScope.launch {
            _events.emit(PlayBoardEvents.PlaySound(UiSound.CARD_SWAP))
        }

    }

    private fun updatePlayersHandCards(seat: Seat,handCards: List<String>){


                _uiState.update {
                    it.copy(
                        playersHandCards = it.playersHandCards + (seat to handCards)
                    )
                }



    }

    private fun finalizeLanding(movingCard: MovingCard){
        val zIndex = _uiState.value.centerTable.size+1
        _uiState.update { state->
            state.copy(
                flyingCards = state.flyingCards.filter { it.card.id != movingCard.card.id},

            )

        }

        if(movingCard.toCenter) {
            _uiState.update { state ->
                state.copy(
                    centerTable = state.centerTable + (movingCard.seat to StaticCard(
                        card = movingCard.card,
                        seat = movingCard.seat,
                        pos = movingCard.endPos,
                        zIndex = movingCard.zIndex
                    ))
                )
            }
        }
    }

    private fun flightTheCardsToWinner(winnerSeat: Seat){
        var flyingCardList = emptyList<MovingCard>()
        _uiState.value.centerTable.forEach {
            val movingCard = MovingCard(
                card = it.value.card,
                seat = it.value.seat,
                startPos= getTargetSlot(_uiState.value.tableCenter,it.key),
                endPos = _uiState.value.seatPositions[winnerSeat]?:Offset.Zero,
                isLocal = false,
                targetRotation = getRotationForSeat(winnerSeat),
                toCenter = false
            )
            flyingCardList = flyingCardList + movingCard
        }

        _uiState.update {
            it.copy(
                flyingCards = flyingCardList,
                centerTable = emptyMap()
            )
        }

        viewModelScope.launch {
            _events.emit(PlayBoardEvents.PlaySound(UiSound.CARD_SWAP))
        }
    }

    private fun throwCardToWinner(){
        _uiState.update {
            it.copy(
                flyingCards = uiState.value.flyingCards.filter { movingCard -> movingCard.toCenter }
            )
        }
    }

    private fun reconnectRoom(reconnectToken:String){

        viewModelScope.launch {
            _uiState.value=_uiState.value.copy(isLoading = true)
            val result = roomServiceRepository.reconnectRoom(reconnectToken)
            result.onSuccess {result->

                println("result: $result")
                _uiState.update {
                    it.copy(
                        room = result.room,
                        localPlayerId = result.playerId
                    )
                }
                mapPlayerIdsWithSeat(result.room.players,result.playerId)
                result.room.game?.roundState?.handCards?.let {
                    it.forEach {
                        if(it.key==result.playerId){
                             updateHandCards(it.value)
                        }
                        else{
                            val seat = _uiState.value.playerSeats[it.key]
                            updatePlayersHandCards(seat!!,it.value)
                        }
                    }

                }
                result.room.game?.roundState?.centerTrickedCard?.let {
                    updateCenterCards(it)
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
        println("hand cards: $handCards")
        val handCard = handCards.map {
            Card.entries.find { card->
                card.id==it
            }?:Card.entries[0]
        }
        _uiState.update {
            it.copy(
                handCard = handCard.sortedBy { it.suit.ordinal * 20 + it.rank.value }
            )

        }
        println("hand cards updated: ${_uiState.value.handCard}")
    }

    private fun updateCenterCards(centerCards:Map<String,String>){

        var centerCardsMap = emptyMap<Seat,StaticCard>()

        centerCards.forEach {
            val card = Card.getCardById(it.value)
            val seat = _uiState.value.playerSeats[it.key]
            centerCardsMap = centerCardsMap + ((seat?:Seat.BOTTOM) to StaticCard(
                card = card,
                seat = seat?:Seat.BOTTOM,
                pos = getTargetSlot(_uiState.value.tableCenter,seat?:Seat.BOTTOM),
                zIndex = 1f
            ))
        }

        _uiState.update {
            it.copy(
                centerTable = centerCardsMap
            )
        }

    }

    private fun playCard(card: Card){

        viewModelScope.launch {
            gameSocketRepository.playCard(
                playerId = _uiState.value.localPlayerId,
                roomId = _uiState.value.room?.id?:"",
                card = card.id
            )
            _uiState.update {
                it.copy(cardPlayed = true)
            }
        }

    }

    private fun playBid(bid:Int){
        viewModelScope.launch {
            gameSocketRepository.placeBet(
                playerId = _uiState.value.localPlayerId,
                roomId = _uiState.value.room?.id ?: "",
                bet = bid
            )
        }
    }

    private fun react(reaction: Reaction){

        viewModelScope.launch {
            gameSocketRepository.sendReaction(
                playerId = _uiState.value.localPlayerId,
                roomId = _uiState.value.room?.id?:"",
                reaction=reaction
            )
        }

    }

    private fun leaveRoom(reconnectToken:String){
        viewModelScope.launch {
            _uiState.value=_uiState.value.copy(isLoading = true)
            val result = roomServiceRepository.leaveRoom(reconnectToken)
            result.onSuccess {
                _events.emit(PlayBoardEvents.NavigateHome)
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



}

