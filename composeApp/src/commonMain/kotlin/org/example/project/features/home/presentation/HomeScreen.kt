package org.example.project.features.entry.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.project.components.Avatar
import org.example.project.core.prefrences.PrefrenceManager
import org.example.project.features.home.presentation.HomeEvents
import org.example.project.features.home.presentation.HomeIntent
import org.example.project.serverRoom.socket.SocketEngine
import org.example.project.ui.component.GlassCard
import org.example.project.ui.component.buttonWithoutRipple
import org.example.project.ui.effects.bouncingClick
import org.example.project.ui.theme.darkPrimaryBlue
import org.example.project.ui.theme.lightPrimaryBlue
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import spadebreaklive.composeapp.generated.resources.Res
import spadebreaklive.composeapp.generated.resources.blue_wooden_background

@Composable
fun HomeScreen(viewModel: HomeViewModel,
               navigateToWaitingRoom:(String,String)->Unit,
               socketEngine: SocketEngine
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewmodel = koinViewModel<HomeViewModel>()
    val prefrenceManager:PrefrenceManager= getKoin().get()


    LaunchedEffect(Unit){
        viewmodel.events.collect{
            when(it){
                is HomeEvents.NavigateToWaitingRoom ->{

                    println("navigating to room: ${it.room.id}")
                    prefrenceManager.saveReconnectToken(it.reconnectionToken)
                    navigateToWaitingRoom(it.room.id,it.playerId)


                }
                is HomeEvents.ShowToast -> println(it.message)
            }
        }
    }

    ModalNavigationDrawer(
        drawerState=drawerState,
        drawerContent = {
            AvatarDrawerContent(
                onChoose = {
                    scope.launch {
                        drawerState.close()
                    }
                    viewmodel.onIntent(HomeIntent.AvatarChanged(it))
                }
            )
        }
    ){

        Box(
            modifier = Modifier.fillMaxSize()
        ) {



            Image(
                painter = painterResource(Res.drawable.blue_wooden_background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Content(viewmodel, onEditAvatarClick = {
                scope.launch {
                    drawerState.open()
                }
            });


        }

    }



}

@Composable
private fun Content(viewModel: HomeViewModel,onEditAvatarClick:()->Unit){
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){


        GlassCard {


            Column(
                modifier = Modifier
                    .width(300.dp)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Box{
                    AvatarImage(avatar = uiState.avatar)
                    IconButton(
                        onClick = onEditAvatarClick,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(30.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.darkPrimaryBlue
                        )
                    ){
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = Color.White
                        )

                    }
                }

                Spacer(Modifier.height(15.dp))

                EditTextField(
                    value = uiState.nickName,
                    onValueChange = { viewModel.onIntent(HomeIntent.NickNameChanged(it)) },
                )

                Spacer(Modifier.height(15.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){

                    buttonWithoutRipple(
                        onClick = { viewModel.onIntent(HomeIntent.CreateRoomClicked) }
                    ){
                        Text("Create Room",
                            style = MaterialTheme.typography.labelLarge)
                    }

                    buttonWithoutRipple(
                        onClick = { /*TODO*/ }
                    ){
                        Text("Join Room",
                            style = MaterialTheme.typography.labelLarge)
                    }

                }
            }

        }

    }
}

//0xFF0c5983
//0xFF076691

@Composable
private fun AvatarImage(modifier: Modifier=Modifier,avatar: Avatar,size: Dp =100.dp){
    Box(modifier = modifier
        .clip(CircleShape)
        .size(size)
        .background(color = MaterialTheme.colorScheme.darkPrimaryBlue)
        .clip(CircleShape)
        .size(size)
        .padding(5.dp)
    ){

        Image(
            painter = painterResource(avatar.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )


    }
}

@Composable
private fun EditTextField(
    value: String,
    onValueChange:(String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String="Nick Name",
){

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(15.dp),
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(15.dp),
            placeholder ={
                Text(placeholder)
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.darkPrimaryBlue,
                unfocusedContainerColor = MaterialTheme.colorScheme.lightPrimaryBlue.copy(0.5f),
                focusedContainerColor = MaterialTheme.colorScheme.lightPrimaryBlue,
                cursorColor = MaterialTheme.colorScheme.darkPrimaryBlue,
                selectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colorScheme.darkPrimaryBlue,
                    backgroundColor = MaterialTheme.colorScheme.darkPrimaryBlue.copy(alpha = 0.4f)
                )
            ),
        )

    }


}

@Composable
private fun AvatarDrawerContent(onChoose:(Avatar)->Unit){


    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(color = MaterialTheme.colorScheme.lightPrimaryBlue)
            .padding(16.dp)
    ) {

        Text(
            text = "Choose Avatar",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Spacer(Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){

            items(Avatar.entries){
                AvatarImage(
                    modifier = Modifier.bouncingClick(onClick = {onChoose(it)}),
                    avatar = it,
                    size = 80.dp
                )
            }

        }


    }




}