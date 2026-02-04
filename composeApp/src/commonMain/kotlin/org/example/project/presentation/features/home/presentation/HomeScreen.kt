package org.example.project.features.entry.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import org.example.project.components.Avatar
import org.example.project.data.local.PrefrenceManager
import org.example.project.data.remote.socket.SocketEngine
import org.example.project.domain.models.dummyPlayers
import org.example.project.domain.models.dummyScore
import org.example.project.presentation.features.board.HelperFunctions
import org.example.project.presentation.features.board.presentation.PlayBoardUiState
import org.example.project.presentation.features.board.presentation.ScoreCell
import org.example.project.presentation.features.home.presentation.HomeEvents
import org.example.project.presentation.features.home.presentation.HomeIntent
import org.example.project.presentation.ui.component.FullScreenBlurredBackgroundLoader
import org.example.project.presentation.ui.component.GlassCard
import org.example.project.presentation.ui.component.buttonWithoutRipple
import org.example.project.presentation.ui.effects.bouncingClick
import org.example.project.presentation.ui.theme.darkPrimaryBlue
import org.example.project.presentation.ui.theme.extraDarkPrimaryBlue
import org.example.project.presentation.ui.theme.lightPrimaryBlue
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import spadebreaklive.composeapp.generated.resources.Res
import spadebreaklive.composeapp.generated.resources.blue_wooden_background
import spadebreaklive.composeapp.generated.resources.exit

@Composable
fun HomeScreen(viewModel: HomeViewModel,
               navigateToWaitingRoom:(String)->Unit,
               socketEngine: SocketEngine
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val viewmodel = koinViewModel<HomeViewModel>()
    val preferenceManager: PrefrenceManager = getKoin().get()
    val uiState=viewmodel.uiState.collectAsState()

    LaunchedEffect(Unit){

        viewmodel.events.collect{
            when(it){
                is HomeEvents.NavigateToWaitingRoom ->{

                    println("navigating to room: ${it.room.id}")
                    preferenceManager.saveReconnectToken(it.reconnectionToken)
                    navigateToWaitingRoom(it.reconnectionToken)
                }
                is HomeEvents.ShowToast -> println(it.message)
            }
        }
    }

    LaunchedEffect(Unit){
        val reconnectToken=preferenceManager.getReconnectToken()
        if(reconnectToken!=null) navigateToWaitingRoom(reconnectToken)
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

    if(uiState.value.isLoading){
        FullScreenBlurredBackgroundLoader()
    }



}

@Composable
private fun Content(viewModel: HomeViewModel,onEditAvatarClick:()->Unit){
    val uiState by viewModel.uiState.collectAsState()
    val showJoinRoomCard = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
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
                        onClick = { showJoinRoomCard.value=true }
                    ){
                        Text("Join Room",
                            style = MaterialTheme.typography.labelLarge)
                    }

                }
            }

        }

    }

    if(showJoinRoomCard.value){
        JoinRoomCard(
            roomId = uiState.enterdRoomId,
            onTextChange = {
                viewModel.onIntent(HomeIntent.RoomIdChanged(it))
            },
            onSubmit = {
                viewModel.onIntent(HomeIntent.JoinRoomClicked)
            },
            onCancelClick = {
                viewModel.onIntent(HomeIntent.CancelClicked)
                showJoinRoomCard.value=false
            }
        )
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

@Composable
fun JoinRoomCard(
    roomId:String,
    onTextChange:(String)->Unit,
    onSubmit:()->Unit,
    onCancelClick: () -> Unit,
){

    Box( modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .background(Color.Black.copy(alpha = 0.5f))
        .zIndex(1f)
        .pointerInput(Unit) {detectTapGestures { onCancelClick() }},
        contentAlignment = Alignment.Center){

        Card(
            modifier = Modifier
                .width(300.dp)
                .padding(16.dp)
                .pointerInput(Unit) {
                    detectTapGestures {}
                },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {


                EditTextField(
                    value = roomId,
                    onValueChange = {
                        onTextChange(it)
                    },
                    placeholder = "Room Id"
                )

                Spacer(Modifier.height(15.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically

                ){

                    buttonWithoutRipple(
                        onClick = { onSubmit() }
                    ){
                        Text("Join Room",
                            style = MaterialTheme.typography.labelLarge)
                    }

                    OutlinedButton(
                        onClick = onCancelClick,
                        border = BorderStroke(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.darkPrimaryBlue
                        ),

                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.darkPrimaryBlue
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 20.dp
                        )
                    ) {
                        Text("Cancel",style = MaterialTheme.typography.labelLarge)
                    }

                }



            }


        }

    }
}



@Composable
private fun ScoreBoard(onDismiss:()->Unit={}){

    val score = dummyScore
    val players = dummyPlayers
    val rounds=5

    var total by remember { mutableStateOf<Map<String, Double>>(emptyMap()) }

    LaunchedEffect(score, players) {
        val result = mutableMapOf<String, Double>()

        players.forEach { player ->
            var sum = 0.0
            score.forEach { round ->
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

                            if(roundNumber-1>=score.size){
                                players.forEach {
                                    ScoreCell(null)
                                }
                            }
                            else{
                                val map= score[roundNumber-1]
                                players.forEach { player ->

                                    val calculated = HelperFunctions.calculateFinalScore(map[player.id])
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
