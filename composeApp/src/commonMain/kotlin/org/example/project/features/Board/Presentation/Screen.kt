package org.example.project.features.Board.Presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.example.project.components.Card
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import spadebreaklive.composeapp.generated.resources.Res
import spadebreaklive.composeapp.generated.resources.red_joker
import spadebreaklive.composeapp.generated.resources.wooden_background

@Composable
fun PlayingBoardScreen() {
 Box(
  modifier = Modifier.fillMaxSize()
 ) {


  Image(
   painter = painterResource(Res.drawable.wooden_background),
   contentDescription = null,
   contentScale = ContentScale.Crop,
   modifier = Modifier.matchParentSize()
  )

  val deck:List<Card> = Card.entries

  val shuffledDeck=deck.shuffled()

  val handCards=shuffledDeck.take(13)

  HandCards(
   cards = handCards,
   modifier = Modifier
    .fillMaxSize()
  )



 }
}


@Composable
private fun HandCards(
 cards: List<Card>,
 modifier: Modifier = Modifier
){

  Box(modifier=Modifier.fillMaxSize(),
   contentAlignment = Alignment.BottomCenter
  ){


   cards.forEachIndexed { index, card ->
    AnimatedCard(
     index = index,
     total = cards.size,
     cardRes = card.image
    )
   }


  }



}

@Composable
fun AnimatedCard(index: Int, total: Int, cardRes: DrawableResource) {
 val scope = rememberCoroutineScope()


 val offsetX = remember { Animatable(0f) }
 val rotation = remember { Animatable(0f) }


 val targetX = (index - total / 2) * 60f
 val targetRotation = (index - total / 2) * 3f

 val baseYOffset = 150f

 LaunchedEffect(Unit) {
  delay(index * 100L)
  scope.launch { offsetX.animateTo(targetX, tween(600)) }
  scope.launch { rotation.animateTo(targetRotation, tween(600)) }
 }

 Image(
  painter = painterResource(cardRes),
  contentDescription = null,
  modifier = Modifier
   .size(
    width = 100.dp,
    height = 150.dp
   )
   .graphicsLayer {
    translationX = offsetX.value
    translationY = baseYOffset
    rotationZ = rotation.value

   }
 )
}




