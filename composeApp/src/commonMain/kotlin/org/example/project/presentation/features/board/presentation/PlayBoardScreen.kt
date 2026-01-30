package org.example.project.presentation.features.board.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.components.Avatar
import org.example.project.components.Card
import org.example.project.domain.models.getPlayerById
import org.example.project.presentation.features.board.getRotationForSeat
import org.example.project.presentation.features.board.getTargetSlot
import org.example.project.presentation.ui.component.GlassCard
import org.example.project.presentation.ui.theme.darkPrimaryBlue
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import spadebreaklive.composeapp.generated.resources.Res
import spadebreaklive.composeapp.generated.resources.blue_wooden_background
import spadebreaklive.composeapp.generated.resources.cards_back

@Composable
fun PlayingBoardScreen(reconnectionToken:String) {

    val viewmodel  = koinViewModel<PlayViewmodel>()

    val uiState by viewmodel.uiState.collectAsState()

    LaunchedEffect(Unit){
        viewmodel.onIntent(PlayBoardIntent.ReconnectRoom(reconnectionToken))
    }


    if(uiState.isLoading){
        CircularProgressIndicator()
    }
    else{
        key(uiState.room?.game?.round){
            Content(reconnectionToken,viewmodel,uiState)
        }

    }

}

@Composable fun Content(reconnectionToken: String,viewmodel: PlayViewmodel,uiState: PlayBoardUiState){
    var centerPosition  by remember{ mutableStateOf(Offset.Zero) }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {



        Image(
            painter = painterResource(Res.drawable.blue_wooden_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

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






        uiState.centerTable.forEach {
            key(it.value.card.id){
                StaticCardOnTable(
                    card = it.value.card,
                    pos = getTargetSlot(
                        centerPosition,
                        it.key
                    ),
                    rotation = getRotationForSeat(
                        it.key
                    ),
                    zIndex = it.value.zIndex
                )
            }
        }

        uiState.flyingCards.forEach {
            key(it.card.id){
                FlyingCard(
                    movingCard = it,
                    tableCenter = centerPosition
                ){
                    viewmodel.onIntent(PlayBoardIntent.FinalyzeCard(it))
                }
            }
        }


        MyHandCard(
            cards = uiState.handCard,
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

        PlayerCardFan(
            seat = Seat.LEFT
        )
        PlayerCardFan(
            seat = Seat.RIGHT
        )
        PlayerCardFan(
            seat = Seat.TOP
        )





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










    }
}


@Composable
private fun MyHandCard(
    cards:List<Card>,
    modifier: Modifier = Modifier,
    onCardPlayed: (card: Card,offset: Offset) -> Unit
){
    Box(modifier=Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ){

        cards.forEachIndexed { index, card ->
            key(card.id) {
                AnimatedHandCard(
                    card = card,
                    index = index,
                    total = cards.size,
                    modifier = modifier,
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
    index:Int,
    total:Int,
    modifier: Modifier = Modifier,
    canPlay:Boolean=true,
    onCardPlayed: (card: Card,offset: Offset) -> Unit
){
    var cardPositionInRoot by remember { mutableStateOf(Offset.Zero) }

    val scope = rememberCoroutineScope()

    val offsetY= remember { Animatable( 80f) }


    val offsetX = remember { Animatable(0f) }
    val scale= remember { Animatable(1f) }
    val rotation = remember { Animatable(0f) }


    val targetX=(index-total/2)*90f
    val targetRotation=(index-total/2)*5f

    LaunchedEffect(Unit) {
        delay(index * 100L)
        scope.launch { offsetX.animateTo(targetX, tween(600)) }
          scope.launch { rotation.animateTo(targetRotation, tween(600)) }
    }




    Image(
        painter = painterResource(card.image),
        contentDescription = null,
        modifier = Modifier
            .size(100.dp)
            .onGloballyPositioned {
                cardPositionInRoot = it.positionInRoot()
            }
            .graphicsLayer{
                translationX = offsetX.value
                translationY = offsetY.value
                rotationZ = rotation.value
            }
            .pointerInput(Unit){
                detectVerticalDragGestures(
                    onDragEnd = {
                        if(offsetY.value<-10f){
                            val startPos= cardPositionInRoot+Offset(offsetX.value,offsetY.value)
                            onCardPlayed(card, startPos)
                        }
                        else{
                            scope.launch {
                                offsetY.animateTo(80f, tween(300))
                                scale.snapTo(1f)
                                rotation.snapTo(targetRotation)
                            }
                        }
                    },
                    onVerticalDrag = { _,dragAmount ->
                        if(canPlay){
                            scope.launch {
                                offsetY.snapTo(offsetY.value + dragAmount)
                                scale.snapTo(1.2f)
                                rotation.snapTo(0f)
                            }
                        }

                    }
                )

            }
    )

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
    val targetPos =
        getTargetSlot(tableCenter, movingCard.seat)
    val targetRotation =
        getRotationForSeat(movingCard.seat)

    val animeOffset = remember { Animatable(movingCard.startPos, Offset.VectorConverter) }

    val rotation = remember { Animatable(targetRotation) }
    val scale = remember { Animatable(if (movingCard.isLocal) 1.2f else 0.7f) }

        LaunchedEffect(targetPos){
            launch{
                animeOffset.animateTo(targetPos, tween(500, easing = FastOutSlowInEasing))
                onFinish()
            }
            launch{ rotation.animateTo(targetRotation, tween(500)) }
            launch{ scale.animateTo(1f, tween(500)) }
        }




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

@Composable
private fun PlayerCardFan(seat: Seat,cardCnt:Int=13){
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

        for(i in 0 until cardCnt){
            FanCard(cardCnt,i,rotation)
        }

    }


}


@Composable
private fun FanCard(total: Int,idx:Int,preRotation: Float){

    var cardPositionInRoot by remember { mutableStateOf(Offset.Zero) }

    val scope = rememberCoroutineScope()

    val offsetY= remember { Animatable( 100f) }


    val offsetX = remember { Animatable(0f) }
    val rotation = remember { Animatable(0f) }


    val targetX=(idx-total/2)*3f
    val targetRotation=(idx-total/2)*10f

    LaunchedEffect(Unit) {
        delay(idx * 100L)
        scope.launch { offsetX.animateTo(targetX, tween(600)) }
        scope.launch { rotation.animateTo(targetRotation, tween(600)) }
    }




    Image(
        painter = painterResource(Res.drawable.cards_back),
        contentDescription = null,
        modifier = Modifier
            .size(100.dp)
            .onGloballyPositioned {
                cardPositionInRoot = it.positionInRoot()
            }
            .rotate(preRotation)
            .graphicsLayer{
                translationX = offsetX.value
                translationY = offsetY.value
                rotationZ = rotation.value
            }
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
            Seat.TOP,Seat.BOTTOM->{
                Row {
                    ScoreAndNameText(player?.nickname?:"player")
                    PlayerAvatar(avatar)
                    ScoreAndNameText("$score/$bet")
                }
            }
            Seat.LEFT,Seat.RIGHT->{
                Column(horizontalAlignment=Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                    ScoreAndNameText(player?.nickname?:"player")
                    PlayerAvatar(avatar)
                    ScoreAndNameText("$score/$bet")
                }
            }
            null->{}
        }

    }








}

@Composable
private fun PlayerAvatar(avatar: Avatar,size: Dp = 50.dp){
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .size(size)
            .background(MaterialTheme.colorScheme.darkPrimaryBlue)
            .padding(5.dp)
    ) {
        Image(
            painter = painterResource(
                avatar.image
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
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

/*
@Composable
private fun HandCards(
 cards: List<Card>,
 modifier: Modifier = Modifier,
 onCardPlayed: (PlayedCard) -> Unit,
 player:Player
){

  Box(modifier=Modifier.fillMaxSize(),
   contentAlignment = Alignment.BottomCenter
  ){


    cards.forEachIndexed { index, card ->
     key(card.id) {
      AnimatedCard(
       index = index,
       total = cards.size,
       cardRes = card.image,
       onCardPlayed = {
        onCardPlayed(
         PlayedCard(
          player = player,
          card = card
         )
        )
       }
      )
     }

   }



  }



}

@Composable
fun AnimatedCard(index: Int,
                 total: Int,
                 cardRes: DrawableResource
                 ,onCardPlayed:()->Unit={}
                 ,canPlay:Boolean=true) {
 val scope = rememberCoroutineScope()

 val offsetY= remember { Animatable(if(canPlay) 60f else 80f) }


 val offsetX = remember { Animatable(0f) }
 val rotation = remember { Animatable(0f) }


 val targetX = (index - total / 2) * 90f
 val targetRotation = (index - total / 2) * 5f



 LaunchedEffect(Unit) {
  delay(index * 100L)
  scope.launch { offsetX.animateTo(targetX, tween(600)) }
  scope.launch { rotation.animateTo(targetRotation, tween(600)) }
 }

 Image(
  painter = painterResource(cardRes),
  contentDescription = null,
  modifier = Modifier
   .size(100.dp)
   .graphicsLayer {
    translationX = offsetX.value
    translationY = offsetY.value
    rotationZ = rotation.value

   }
   .pointerInput(Unit){
    detectVerticalDragGestures(
     onDragEnd = {
      if(offsetY.value<-10f){
       onCardPlayed()
       scope.launch {
        offsetX.animateTo(0f, tween(600))
       }
       scope.launch {
        offsetY.animateTo(-190f, tween(600))
       }
      }
      else{
       scope.launch {
        offsetY.animateTo(if(canPlay) 60f else 80f, tween(300))
        rotation.snapTo(targetRotation)
       }
      }
     },
     onVerticalDrag = { _,dragAmount ->
      if(canPlay){
       scope.launch {
        offsetY.snapTo(offsetY.value + dragAmount)
        rotation.snapTo(0f)
       }
      }

     }
    )

   }
 )
}







@Composable
fun CenterPlayedCards(modifier: Modifier=Modifier.size(200.dp),centerCards: List<PlayedCard>) {
 Box(
  modifier = modifier,
 ) {

  centerCards.forEach {

   val (alignment, rotation ) = when (it.player.seat) {
    1 -> Alignment.BottomCenter to 0f
    2 -> Alignment.CenterEnd to -90f
    3 -> Alignment.TopCenter to 180f
    4 -> Alignment.CenterStart to 90f
    else -> Alignment.Center to 0f
   }

   Image(
    painter = painterResource(it.card.image),
    contentDescription = null,
    modifier = Modifier
     .size(100.dp)
     .align(alignment)
     .graphicsLayer { rotationZ = rotation }
   )



  }

 }

}*/

