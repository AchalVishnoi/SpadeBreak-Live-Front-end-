package org.example.project.presentation.features.board.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Score
import androidx.compose.material.icons.filled.Scoreboard
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.components.Avatar
import org.example.project.components.Card
import org.example.project.components.Reaction
import org.example.project.components.getAnimation
import org.example.project.components.showReactionAnimation
import org.example.project.data.local.PrefrenceManager
import org.example.project.domain.models.RoundStatus
import org.example.project.domain.models.getPlayerById
import org.example.project.presentation.features.board.GlowingCircularBackground
import org.example.project.presentation.features.board.HelperFunctions
import org.example.project.presentation.features.board.getRotationForSeat
import org.example.project.presentation.features.board.getTargetSlot
import org.example.project.presentation.ui.component.FullScreenBlurredBackgroundLoader
import org.example.project.presentation.ui.component.FullScreenLoader
import org.example.project.presentation.ui.component.GlassCard
import org.example.project.presentation.ui.effects.bouncingClick
import org.example.project.presentation.ui.theme.darkPrimaryBlue
import org.example.project.presentation.ui.theme.extraDarkPrimaryBlue
import org.example.project.presentation.ui.theme.greenColor
import org.example.project.presentation.ui.theme.lightPrimaryBlue
import org.example.project.presentation.ui.theme.lightRedColor
import org.example.project.presentation.utils.RenderLottieFile
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import spadebreaklive.composeapp.generated.resources.Res
import spadebreaklive.composeapp.generated.resources.blue_wooden_background
import spadebreaklive.composeapp.generated.resources.card_back_blue
import spadebreaklive.composeapp.generated.resources.card_back_red
import spadebreaklive.composeapp.generated.resources.cards_back
import spadebreaklive.composeapp.generated.resources.emogi_icon
import spadebreaklive.composeapp.generated.resources.exit
import spadebreaklive.composeapp.generated.resources.like
import spadebreaklive.composeapp.generated.resources.score_card_icon
import spadebreaklive.composeapp.generated.resources.tick_icon

@Composable
fun PlayingBoardScreen(reconnectionToken:String,navigateBack:(reconnectToken:String)->Unit,navigateToHomeScreen:()->Unit) {

    val viewmodel  = koinViewModel<PlayViewmodel>()

    val uiState by viewmodel.uiState.collectAsState()

    LaunchedEffect(Unit){
        viewmodel.onIntent(PlayBoardIntent.ReconnectRoom(reconnectionToken))
    }

    val prefrenceManager:PrefrenceManager = getKoin().get()
    LaunchedEffect(Unit){
        viewmodel.events.collect{
            when(it){
                is PlayBoardEvents.CardPlayed -> {}
                PlayBoardEvents.NavigateBack -> {navigateBack(reconnectionToken)}
                PlayBoardEvents.ShowScoreCard -> {}
                is PlayBoardEvents.ShowToast -> {}
                is PlayBoardEvents.TrickWinner -> {}
                PlayBoardEvents.NavigateHome -> {
                    prefrenceManager.deleteToken()
                    navigateToHomeScreen()
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {


        Image(
            painter = painterResource(Res.drawable.blue_wooden_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        if (uiState.isLoading) {
            FullScreenBlurredBackgroundLoader()
        }
            uiState.room?.game?.roundState?.let{
                if(it.status==RoundStatus.BETTING&& it.playerTurn == uiState.localPlayerId){
                    BiddingCard {bid->
                        viewmodel.onIntent(PlayBoardIntent.PlayBid(bid))
                    }
                }
            }

            if(uiState.showScoreCard){
                ScoreBoard(
                    uiState = uiState,
                    modifier = Modifier
                ){
                    viewmodel.onIntent(PlayBoardIntent.HideScoreCard)
                    if(uiState.gameCompleted){
                        navigateBack(reconnectionToken)
                    }
                }
            }
            key(uiState.room?.game?.round) {
                Content(reconnectionToken, viewmodel, uiState)
            }


       }

    }

@Composable fun Content(reconnectionToken: String,viewmodel: PlayViewmodel,uiState: PlayBoardUiState){
    var centerPosition  by remember{ mutableStateOf(Offset.Zero) }


    Box(modifier = Modifier.fillMaxSize()){


        Box(
            modifier = Modifier
                .size(90.dp)
                .align(Alignment.Center)
                .onGloballyPositioned {
                    centerPosition = it.positionInRoot()
                }
        )



        LaunchedEffect(centerPosition){
            viewmodel.onIntent(PlayBoardIntent.UpdateTableCenter(centerPosition))
        }



        ShowScoreCardButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
        ){
            viewmodel.onIntent(PlayBoardIntent.ShowScoreCard)
        }

        LeaveRoomButton(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
        ){
            viewmodel.onIntent(PlayBoardIntent.LeaveRoom(reconnectionToken))

        }








        MyHandCard(
            uiState = uiState,
            modifier = Modifier
        ){card,offset->
            println("card ${card.id} played")
            viewmodel.onIntent(
                PlayBoardIntent.CardPlayed(
                    card = card,
                    playerId = "playerId",
                    offset = offset
                )
            )
        }

        uiState.playersHandCards[Seat.LEFT]?.let {
            PlayerCardFan(
                seat = Seat.LEFT,
                cardList = it
            )
        }

        uiState.playersHandCards[Seat.RIGHT]?.let {
            PlayerCardFan(
                seat = Seat.RIGHT,
                cardList = it
            )
        }
        uiState.playersHandCards[Seat.TOP]?.let {
            PlayerCardFan(
                seat = Seat.TOP,
                cardList = it
            )
        }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f)
        ) {
            uiState.centerTable.forEach {
                key(it.value.card.id) {
                    StaticCardOnTable(
                        card = it.value.card,
                        pos = getTargetSlot(centerPosition, it.key),
                        rotation = getRotationForSeat(it.key),
                        zIndex = 1f
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(10f)
        ) {
            uiState.flyingCards.forEach {
                key(it.card.id) {
                    FlyingCard(
                        movingCard = it,
                        tableCenter = centerPosition
                    ) {
                        viewmodel.onIntent(PlayBoardIntent.FinalyzeCard(it))
                    }
                }
            }
        }







        uiState.playerSeats.forEach {
            key(it.value){
                PlayerUi(
                    playerId = it.key,
                    uiState = uiState,
                    updatePlayerOffset = { offset, seat ->
                        viewmodel.onIntent(PlayBoardIntent.UpdateSeatPosition(seat, offset))
                    }
                )
            }
        }

        ReactionLayer(
            reactionList = uiState.reactionsList,
            modifier = Modifier.fillMaxSize()
        )

        ReactionPopupButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp)
        ){
            viewmodel.onIntent(PlayBoardIntent.ReactionMessage(it))
        }

        uiState.room?.game?.roundState?.status?.let{
            if(it==RoundStatus.BETTING){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "${uiState.room.players.getPlayerById(uiState.room.game.roundState.playerTurn)?.nickname?:"player"} is bidding.....",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White
                    )
                }
            }
        }



    }






}


@Composable
private fun MyHandCard(
    uiState: PlayBoardUiState,
    modifier: Modifier = Modifier,
    onCardPlayed: (card: Card,offset: Offset) -> Unit
){
    Box(modifier=Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ){

        uiState.handCard.forEachIndexed { index, card ->
            key(card.id) {
                AnimatedHandCard(
                    card = card,
                    index = index,
                    total = uiState.handCard.size,
                    modifier = modifier,
                    canPlay = HelperFunctions.canPlay(
                        card,
                        uiState
                    ),
                    onCardPlayed = {card,offset->
                        onCardPlayed(card,offset)
                    }
                )
            }
        }

    }


}



@Composable
private fun AnimatedHandCard(
    card: Card,
    index: Int,
    total: Int,
    modifier: Modifier = Modifier,
    canPlay: Boolean = true,
    onCardPlayed: (card: Card, offset: Offset) -> Unit
) {
    var cardPositionInRoot by remember { mutableStateOf(Offset.Zero) }
    var cardSize by remember { mutableStateOf(IntSize.Zero) }

    val scope = rememberCoroutineScope()

    val offsetY = remember { Animatable(40f) }
    val offsetX = remember { Animatable(0f) }
    val scale = remember { Animatable(1f) }
    val rotation = remember { Animatable(0f) }

    val targetX = (index - total / 2f) * 90f
    val targetRotation = (index - total / 2f) * 5f

    LaunchedEffect(Unit) {
        delay(index * 100L)
        scope.launch { offsetX.animateTo(targetX, tween(600)) }
        scope.launch { rotation.animateTo(targetRotation, tween(600)) }
    }

    val painter = painterResource(card.image)
    val aspectRatio =
        painter.intrinsicSize.width / painter.intrinsicSize.height

    Box(
        modifier = modifier
            .graphicsLayer {
                translationX = offsetX.value
                translationY = offsetY.value
                rotationZ = rotation.value
            }
            .onGloballyPositioned {
                cardPositionInRoot = it.positionInRoot()
                cardSize = it.size
            }
            .pointerInput(canPlay) {
                if (!canPlay) return@pointerInput

                detectVerticalDragGestures(
                    onDragEnd = {
                        if (offsetY.value < -10f) {
                            val startPos = cardPositionInRoot + Offset(offsetX.value, offsetY.value)
                            onCardPlayed(card, startPos)
                        } else {
                            scope.launch {
                                offsetY.animateTo(80f, tween(300))
                                rotation.snapTo(targetRotation)
                            }
                        }
                    },
                    onVerticalDrag = { _, dragAmount ->
                        scope.launch {
                            offsetY.snapTo(offsetY.value + dragAmount)
                            rotation.snapTo(0f)
                        }
                    }
                )
            }
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .height(100.dp)
                .aspectRatio(aspectRatio)
        )

        if (!canPlay) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Black.copy(alpha = 0.45f))
            )
        }
    }

}


@Composable
private fun StaticCardOnTable(
    card: Card,
    pos: Offset,
    rotation: Float,
    zIndex: Float = 1f
) {

    println("center pos: $pos")

    Image(
        painter = painterResource(card.image),
        contentDescription = null,
        modifier = Modifier
            .size(90.dp)
            .offset {
                IntOffset(
                    pos.x.toInt(),
                    pos.y.toInt()
                )
            }
            .zIndex(zIndex)
            .graphicsLayer {
                rotationZ = rotation
            }
    )
}


@Composable
private fun FlyingCard(movingCard: MovingCard
                       ,tableCenter:Offset
                       ,onFinish:()->Unit){

    val targetRotation = movingCard.targetRotation

    val animeOffset = remember { Animatable(movingCard.startPos, Offset.VectorConverter) }

    val rotation = remember { Animatable(movingCard.currRotation) }
    val scale = remember { Animatable(if (movingCard.isLocal) 1f else 0.7f) }

        LaunchedEffect(movingCard.endPos){
            launch{
                animeOffset.animateTo(movingCard.endPos, tween(500, easing = FastOutSlowInEasing))
                onFinish()
            }
            launch{ rotation.animateTo(targetRotation, tween(500)) }
            launch{ scale.animateTo(1f, tween(500)) }
        }



    val visibleState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(animationSpec = tween(0)),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = 0
            )
        )
    ) {
        Image(
            painter = painterResource(movingCard.card.image),
            contentDescription = null,
            modifier = Modifier
                .size(90.dp)
                .offset { IntOffset(animeOffset.value.x.toInt(), animeOffset.value.y.toInt()) }
                .zIndex(movingCard.zIndex)
                .graphicsLayer {
                    rotationZ = rotation.value
                    scaleX = scale.value
                    scaleY = scale.value
                }
        )
    }


}

@Composable
private fun PlayerCardFan(seat: Seat,cardList:List<String> = emptyList()){
    val rotation = getRotationForSeat(seat)
    val alignment = when(seat){
        Seat.BOTTOM -> Alignment.BottomCenter
        Seat.LEFT -> Alignment.CenterStart
        Seat.TOP -> Alignment.TopCenter
        Seat.RIGHT -> Alignment.CenterEnd
    }



    Box(modifier = Modifier
        .rotate(rotation)
        .graphicsLayer {
            rotationZ=rotation
        }
        .fillMaxSize(),
        contentAlignment = alignment){

        cardList.forEachIndexed { i,card->
            FanCard(cardList.size,i,rotation,seat)
        }

    }


}

@Composable
private fun FanCard(total: Int,idx:Int,preRotation: Float,seat: Seat){

    var cardPositionInRoot by remember { mutableStateOf(Offset.Zero) }

    val scope = rememberCoroutineScope()

    val offsetY= remember { Animatable( if(seat==Seat.TOP) 0f else -60f) }


    val offsetX = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }


    val targetX=(idx-total/2)*4f
    val targetRotation=(idx-total/2)*7f

    LaunchedEffect(Unit) {
        delay(idx * 100L)
        scope.launch { offsetX.animateTo(targetX, tween(600)) }
        scope.launch { rotation.animateTo(targetRotation, tween(600)) }
    }




    Image(
        painter = painterResource(Res.drawable.card_back_red),
        contentDescription = null,
        modifier = Modifier
            .onGloballyPositioned {
                cardPositionInRoot = it.positionInRoot()
            }
            .rotate(preRotation)
            .graphicsLayer{
                translationX = offsetX.value
                translationY = offsetY.value
                rotationZ = rotation.value
            }
            .clip(RoundedCornerShape(5.dp))
            .height(70.dp)
    )

}

@Composable
private fun PlayerUi(playerId:String,uiState: PlayBoardUiState,updatePlayerOffset:(Offset,Seat)->Unit){

    val seat=uiState.playerSeats.get(playerId)

    val player= uiState.room?.players?.getPlayerById(playerId)

    var avatar = Avatar.entries[0]

    player?.avatar?.let { avatar=Avatar.valueOf(it) }

    val alignment = when(seat){
        Seat.BOTTOM -> Alignment.BottomCenter
        Seat.LEFT -> Alignment.CenterStart
        Seat.TOP -> Alignment.TopCenter
        Seat.RIGHT -> Alignment.CenterEnd
        null -> Alignment.Center
    }

    var bet=0
    var score=0;

    uiState.room?.game?.roundState?.score?.let {
        bet=it[playerId]?.bet?:0
        score=it[playerId]?.currPoints?:0
    }

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = alignment){

        Box(
            modifier = Modifier.size(90.dp)
                .align(alignment)
                .onGloballyPositioned {
                   updatePlayerOffset(it.positionInRoot(),seat?:Seat.TOP)
                }
        )


        when(seat){
            Seat.TOP->{
                Row {
                    ScoreAndNameText(player?.nickname?:"player")
                    PlayerAvatar(avatar,isActiveTurn = uiState.room?.game?.roundState?.playerTurn==playerId)
                    ScoreAndNameText("$score/$bet")
                }
            }
            Seat.BOTTOM->{
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    ScoreAndNameText(player?.nickname?:"player")
                    PlayerAvatar(avatar,isActiveTurn = uiState.room?.game?.roundState?.playerTurn==playerId)
                    ScoreAndNameText("$score/$bet")
                }
            }
            Seat.LEFT->{
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    ScoreAndNameText(player?.nickname?:"player")
                    PlayerAvatar(avatar,isActiveTurn = uiState.room?.game?.roundState?.playerTurn==playerId)
                    ScoreAndNameText("$score/$bet")
                }
            }
            Seat.RIGHT->{
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    ScoreAndNameText(player?.nickname?:"player")
                    PlayerAvatar(avatar,isActiveTurn = uiState.room?.game?.roundState?.playerTurn==playerId)
                    ScoreAndNameText("$score/$bet")
                }
            }
            null->{}
        }

    }








}

@Composable
private fun PlayerAvatar(avatar: Avatar,size: Dp = 50.dp,isActiveTurn:Boolean=false){
    Box(
        contentAlignment = Alignment.Center
    ) {

        if (isActiveTurn) {
            GlowingCircularBackground(
                modifier = Modifier
                    .size(size + 15.dp)
                    .clip(CircleShape)
            )
        }


        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(if(!isActiveTurn)MaterialTheme.colorScheme.darkPrimaryBlue else Color.Transparent)
                .padding(4.dp)
        ) {
            Image(
                painter = painterResource(avatar.image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().clip(CircleShape)
            )
        }
    }
}

@Composable
fun ReactionLayer(
    reactionList: List<ReactionMessage>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {

        reactionList.forEach { reactionMessage ->

            val (alignment, baseOffset) = when (reactionMessage.seat) {
                Seat.BOTTOM -> Alignment.BottomCenter to IntOffset(0, -120)
                Seat.TOP -> Alignment.TopCenter to IntOffset(0, 120)
                Seat.LEFT -> Alignment.CenterStart to IntOffset(120, 0)
                Seat.RIGHT -> Alignment.CenterEnd to IntOffset(-120, 0)
            }

            Box(
                modifier = Modifier
                    .align(alignment)
                    .offset {
                        IntOffset(
                            baseOffset.x + reactionMessage.offsetX,
                            baseOffset.y + reactionMessage.offsetY
                        )
                    }
                    .size(reactionMessage.size.dp)
            ) {
                AnimatedVisibility(
                    visible = true,
                    enter = scaleIn(initialScale = 0.4f) + fadeIn(),
                    exit = scaleOut(targetScale = 0.4f) + fadeOut()
                ) {
                        RenderLottieFile(
                            file = reactionMessage.reaction.showReactionAnimation() ?: 0,
                            modifier = Modifier.fillMaxSize()
                        )

                }
            }
        }
    }
}

@Composable
private fun ScoreAndNameText(text:String){
    GlassCard {
       Card(
           modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
           shape = RoundedCornerShape(2.dp),
           colors = CardDefaults.cardColors(
               containerColor = Color.Transparent
           )
       ){

           Text(
               text = text,
               style = MaterialTheme.typography.labelMedium
           )

       }
    }
}

@Composable
private fun ShowScoreCardButton(modifier: Modifier,onClick:()->Unit){

    Card(
        modifier = modifier.wrapContentSize(),
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.darkPrimaryBlue
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
    ){
        IconButton(
            onClick = onClick,
            modifier = Modifier
        ){
            Icon(
                painter = painterResource(Res.drawable.score_card_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.lightPrimaryBlue,
                modifier = Modifier.padding(5.dp)
            )
        }
    }

}

@Composable
private fun LeaveRoomButton(modifier: Modifier,onClick:()->Unit){

    Card(
        modifier = modifier.wrapContentSize(),
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.darkPrimaryBlue
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
    ){
        IconButton(
            onClick = onClick,
            modifier = Modifier
        ){
            Icon(
                painter = painterResource(Res.drawable.exit),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.lightPrimaryBlue,
                modifier = Modifier.padding(5.dp)
            )
        }
    }

}

@Composable
private fun ScoreBoard(uiState: PlayBoardUiState,modifier: Modifier,onDismiss:()->Unit){

    val score = uiState.room?.game?.score
    val players = uiState.room?.players?:emptyList()

    val rounds=5

    var total by remember { mutableStateOf<Map<String, Double>>(emptyMap()) }

    LaunchedEffect(score, players) {
        val result = mutableMapOf<String, Double>()

        players.forEach { player ->
            var sum = 0.0
            score?.forEach { round ->
                val roundScore = round[player.id]
                sum += HelperFunctions.calculateFinalScore(roundScore)?:0.0
            }
            result[player.id] = sum
        }

        total = result
    }

    Box( modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .background(Color.Black.copy(alpha = 0.5f))
        .zIndex(10f)
        .pointerInput(Unit) {detectTapGestures { onDismiss() }},
        contentAlignment = Alignment.Center){

        DismissButton(
            modifier = Modifier
                .padding(40.dp)
                .align(Alignment.TopEnd),
            onClick = onDismiss
        )



        Card(
            modifier = Modifier
                .padding( horizontal = 70.dp)
                .wrapContentSize()
                .padding(vertical = 10.dp, horizontal = 20.dp),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.extraDarkPrimaryBlue
            )
        ){

            Column {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){

                    Text(
                        text = "SCORE BOARD",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.lightPrimaryBlue
                    )

                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    TableCell("Round", true)
                    players.forEach {
                        TableCell(it.nickname, true)
                    }
                }
                DottedDivider()

                for(roundNumber in 1..rounds){

                    Row(modifier = Modifier.fillMaxWidth()) {
                        TableCell("R${(roundNumber)}")

                        if(roundNumber-1>=score?.size?:0){
                            players.forEach {
                                ScoreCell(null)
                            }
                        }
                        else{
                            val map= score?.get(roundNumber-1)
                            players.forEach { player ->

                                val calculated = HelperFunctions.calculateFinalScore(map?.get(player.id))
                                ScoreCell(calculated)

                            }
                        }


                    }
                    DottedDivider()

                }


                DottedDivider(color = MaterialTheme.colorScheme.lightPrimaryBlue.copy(0.7f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TableCell("TOTAL", true)

                    players.forEach {
                        TableCell(total[it.id]?.toString() ?: "0", true)
                    }
                }

            }

        }

    }

}

@Composable
fun RowScope.TableCell(text: String, isHeader: Boolean = false) {
    Box(
        modifier = Modifier
            .weight(1f)
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = if(isHeader) MaterialTheme.typography.titleMedium else MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.lightPrimaryBlue
        )
    }
}

@Composable
fun RowScope.ScoreCell(score: Double?) {
    Box(
        modifier = Modifier
            .weight(1f)
            .padding(3.dp),
        contentAlignment = Alignment.Center
    ) {
        if (score!=null&&score < 0) {
            Box(
                modifier = Modifier
                    .padding(3.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Red,
                        shape = CircleShape
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = score.toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        } else {
            Text(
                text = score?.toString() ?: "",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun DottedDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.lightPrimaryBlue.copy(0.5f)
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        val dashWidth = 10f
        val dashGap = 6f
        val strokeWidth = 2f

        drawLine(
            color = color,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = strokeWidth,
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(dashWidth, dashGap)
            )
        )
    }
}

@Composable
private fun DismissButton(modifier: Modifier,onClick:()->Unit){

    Card(
        modifier = modifier.wrapContentSize(),
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ){
        IconButton(
            onClick = onClick,
            modifier = Modifier
        ){
            Text(
                text = "X",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.lightPrimaryBlue
            )
        }
    }

}

@Composable
private fun BiddingCard(onSelected:(Int)->Unit){
    var selectedBid by remember { mutableStateOf(1) }
    Box( modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .background(Color.Black.copy(alpha = 0.5f))
        .zIndex(20f)
        .pointerInput(Unit) {detectTapGestures {}},
        contentAlignment = Alignment.Center){

        Card(
            modifier = Modifier
                .width(400.dp)
                .wrapContentSize(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.lightPrimaryBlue
            )
        ){

            Column(
                modifier = Modifier.padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, bottom = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select your bid!",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 10.dp),
                        color = MaterialTheme.colorScheme.extraDarkPrimaryBlue
                    )
                    RightClickButton(
                        modifier = Modifier.size(30.dp)
                    ){
                        onSelected(selectedBid)
                    }

                }


                FlowRow {
                    for(i in 1..11){
                        BidButton(
                            isSelected = selectedBid==i,
                            value = i,
                            onClick = {selectedBid=i}
                        )
                    }
                }
            }



        }


    }

}

@Composable
private fun BidButton(isSelected:Boolean,value:Int,onClick:()->Unit){
    Card(
        modifier = Modifier
            .padding(5.dp)
            .size(
                width = 30.dp,
                height = 30.dp
            )
            .bouncingClick { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if(isSelected) MaterialTheme.colorScheme.darkPrimaryBlue else Color.Transparent
        ),
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.extraDarkPrimaryBlue
        )
    ){

        Box(modifier = Modifier
            .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ){

            Text(
                text = value.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = if(isSelected) MaterialTheme.colorScheme.lightPrimaryBlue else MaterialTheme.colorScheme.extraDarkPrimaryBlue
            )

        }

    }
}

@Composable
private fun RightClickButton(modifier: Modifier,onClick:()->Unit){

    Card(
        modifier = modifier
            .wrapContentSize()
            .bouncingClick { onClick() },
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.greenColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        ),
    ){
        Box(
            modifier=Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Icon(
                painter = painterResource(Res.drawable.tick_icon) ,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(5.dp)
            )
        }

    }

}

@Composable
private fun ReactionPopupButton(modifier: Modifier,onReactionClick: (reaction: Reaction) -> Unit){
    var isPopUp by remember { mutableStateOf(false) }

    Box(modifier=modifier, contentAlignment = Alignment.Center){
        if(!isPopUp){
            Card(
                modifier = Modifier.wrapContentSize(),
                shape = RoundedCornerShape(50),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.extraDarkPrimaryBlue
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 5.dp
                ),
            ){
                IconButton(
                    onClick = {isPopUp=true},
                    modifier = Modifier
                ){
                    Icon(
                        painter = painterResource(Res.drawable.emogi_icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.lightPrimaryBlue,
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        }
        else{
            ReactionList(
                onReactionClick = onReactionClick,
                onDismissRequest = {isPopUp=false}
            )
        }
    }

}

@Composable
private fun ReactionList(modifier: Modifier=Modifier, onReactionClick:(reaction: Reaction)->Unit, onDismissRequest:()->Unit){
    val reactionList = Reaction.entries

    Popup(
        alignment = Alignment.Center,
        offset = IntOffset(-200, -200),
        properties = PopupProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        onDismissRequest = onDismissRequest
    ) {
        Box(
            modifier = Modifier.wrapContentSize()
        ) {
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .widthIn(max = 220.dp),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.lightPrimaryBlue
                ),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .wrapContentSize()
                        . padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(reactionList) { reaction ->
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray.copy(alpha = 0.3f))
                                .clickable {
                                    onReactionClick(reaction)
                                    onDismissRequest()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            RenderLottieFile(
                                file = reaction.getAnimation() ?: 0,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}