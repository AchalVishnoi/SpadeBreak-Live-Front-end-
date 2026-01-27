package org.example.project.presentation.features.board.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MonotonicFrameClock
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import org.example.project.components.Card
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PlayRoomScreen(){

    val viewmodel= koinViewModel<PlayViewmodel>()
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Image(
            painter = painterResource(Card.entries[0].image),
            contentDescription = null,
            modifier = Modifier
                .size(90.dp)
                .offset { IntOffset(700,270) }
                .graphicsLayer {
                    rotationZ = 90f
                }
        )
    }


}