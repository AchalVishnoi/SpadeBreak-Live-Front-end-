package org.example.project.presentation.features.waitingRoom.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraOutdoor
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import org.example.project.components.Avatar
import org.example.project.data.local.PrefrenceManager
import org.example.project.presentation.utils.SoundPlayer
import org.example.project.domain.models.Player
import org.example.project.domain.models.getPlayerById
import org.example.project.presentation.toast.ToastManager
import org.example.project.presentation.ui.component.FullScreenBlurredBackgroundLoader
import org.example.project.presentation.ui.component.GlassCard
import org.example.project.presentation.ui.effects.bouncingClick
import org.example.project.presentation.ui.theme.darkPrimaryBlue
import org.example.project.presentation.ui.theme.extraDarkPrimaryBlue
import org.example.project.presentation.ui.theme.lightPrimaryBlue
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.getKoin
import spadebreaklive.composeapp.generated.resources.Res
import spadebreaklive.composeapp.generated.resources.blue_wooden_background
import spadebreaklive.composeapp.generated.resources.exit
import spadebreaklive.composeapp.generated.resources.like

@Composable
fun WaitingRoomScreen(waitingRoomViewModel: WaitingRoomViewModel,reconnectToken:String,onBack:()->Unit,navigateToPlayRoom: (String)->Unit) {

    println("navigated to waiting room")
    val soundPlayer: SoundPlayer =getKoin().get()
    val prefrenceManager=getKoin().get<PrefrenceManager>()

    val arr = reconnectToken.split(":")
    val roomId=arr[0]
    val playerId=arr[1]
    val toastManager = getKoin().get<ToastManager>()


    LaunchedEffect(Unit){
        waitingRoomViewModel.onIntent(WaitingRoomIntent.Connect(reconnectToken))
    }

    val uiState=waitingRoomViewModel.uiState.collectAsState()

    LaunchedEffect(Unit){
        waitingRoomViewModel.events.collect(){
            when(it){
                is WaitingRoomEvents.ShowToast -> toastManager.showToast(it.message)
                is WaitingRoomEvents.PlaySound ->{
                    soundPlayer.playSound(it.uiSound)
                }
                is WaitingRoomEvents.NavigateToPlayRoom->{
                      navigateToPlayRoom(reconnectToken)
                }

                WaitingRoomEvents.NavigateBack ->{
                    prefrenceManager.deleteToken()
                    onBack()
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){


        Image(
            painter = painterResource(Res.drawable.blue_wooden_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        IconButton(
            onClick = { waitingRoomViewModel.onIntent(WaitingRoomIntent.LeaveRoom(reconnectToken)) },
            modifier = Modifier
                .padding(20.dp)
                .clip(CircleShape)
                .background(color = MaterialTheme.colorScheme.extraDarkPrimaryBlue, shape = CircleShape)
                .align(Alignment.TopStart)
        ){
            Icon(
                painter = painterResource(Res.drawable.exit),
                contentDescription = null,
                tint = Color.White
            )
        }


        uiState.value.room?.players?.getPlayerById(uiState.value.playerId?:"1")?.let {

            println("is ready button block called")
            if(!it.ready){
                println("is ready button block called")
                IsReadyButton(
                    alignment = Alignment.BottomEnd,
                    uiState = uiState.value,
                    onClick = {
                        waitingRoomViewModel.onIntent(WaitingRoomIntent.Ready)
                    }
                )
            }
        }






        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            ShowRoomId(roomId)
            uiState.value.room?.players?.let {
                ShowPlayerList(it)
            }

        }

        if(uiState.value.isLoading){
            FullScreenBlurredBackgroundLoader()
        }


    }
}

@Composable
private fun ShowPlayerList(players: List<Player>){
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){


            players.forEach {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(10.dp)) {
                    PlayerCard(it, modifier = Modifier,100.dp)
                    Spacer(Modifier.height(10.dp))
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn()
                    ){

                        GlassCard {

                            Text(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                text = it.nickname,
                                style = MaterialTheme.typography.labelLarge
                            )

                        }

                    }

            }


        }

    }

}

@Composable
private fun ShowRoomId(roomId: String){
    GlassCard {

        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center

        ){
            Text(
                text = "Room Id: $roomId",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
        }

    }
}

@Composable
private fun PlayerCard(player: Player, modifier: Modifier, size: Dp = 100.dp) {

    val visibleState = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    AnimatedVisibility(
        visibleState = visibleState,
        enter = scaleIn(
            initialScale = 0.2f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessHigh
            )
        ),
        exit = scaleOut(
            targetScale = 0.0f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessHigh
            )
        )
    ) {
        Box {
            Box(
                modifier = modifier
                    .clip(CircleShape)
                    .size(size)
                    .background(MaterialTheme.colorScheme.darkPrimaryBlue)
                    .padding(5.dp)
            ) {
                Image(
                    painter = painterResource(
                        Avatar.fromId(player.avatar ?: Avatar.AVATAR_01.id)!!.image
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }

            AnimatedVisibility(
                visible = player.ready,
                enter = scaleIn(
                    initialScale = 0.2f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessHigh
                    )
                ),
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Image(
                    painter = painterResource(Res.drawable.like),
                    contentDescription = null,
                    modifier=Modifier
                )
            }
        }
    }
}

@Composable
private fun IsReadyButton(alignment: Alignment=Alignment.BottomEnd,uiState: WaitingRoomUiState,onClick:()->Unit){
Box(modifier = Modifier.fillMaxSize().zIndex(1f), contentAlignment = alignment){

    if(uiState.isReadyLoading){
        CircularProgressIndicator(
            modifier = Modifier
                .align(alignment)
                .padding(50.dp)
                .size(50.dp),
            color = MaterialTheme.colorScheme.extraDarkPrimaryBlue,
            strokeWidth = 5.dp

        )
    }
    else{
        Card(
            modifier = Modifier
                .align(alignment)
                .padding(50.dp)
                .size(50.dp)
                .bouncingClick{
                    onClick()
                },
            shape = CircleShape,

            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.extraDarkPrimaryBlue
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ){

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.lightPrimaryBlue
                )
            }

        }
    }

}
}
