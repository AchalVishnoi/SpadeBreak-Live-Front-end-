package org.example.project.features.board.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.components.Card
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import spadebreaklive.composeapp.generated.resources.Res
import spadebreaklive.composeapp.generated.resources.blue_wooden_background

@Composable
fun PlayingBoardScreen() {
 Box(
  modifier = Modifier.fillMaxSize()
 ) {

  val viewmodel  = koinViewModel<PlayViewmodel>()

  val uiState by viewmodel.uiState.collectAsState()

   Image(
     painter = painterResource(Res.drawable.blue_wooden_background),
     contentDescription = null,
     contentScale = ContentScale.Crop,
     modifier = Modifier.matchParentSize()
   )



   CenterPlayedCards(
    Modifier.size(190.dp).align(Alignment.Center),
    uiState.centerPlayedCards
   )




   HandCards(
      cards = uiState.player1.handCards,
      modifier = Modifier
       .fillMaxSize(),
      onCardPlayed = {
       viewmodel.onIntent(PlayBoardIntent.CardPlayed(it.card, it.player))
      },
      player = uiState.player1
   )



 }
}


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

}