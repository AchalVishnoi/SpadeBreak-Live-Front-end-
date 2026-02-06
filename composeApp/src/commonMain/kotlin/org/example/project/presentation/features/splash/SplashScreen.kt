package org.example.project.presentation.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.example.project.data.local.PrefrenceManager
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import spadebreaklive.composeapp.generated.resources.Res
import spadebreaklive.composeapp.generated.resources.spades_logo

@Composable
fun SplashScreen(navigateToWaitingRoom: (String) -> Unit, navigateToPlayRoom: (String) -> Unit, navigateToHome: () -> Unit) {

    val viewModel = koinViewModel<SplashViewModel>()
    val preferenceManager = getKoin().get<PrefrenceManager>()

    LaunchedEffect(Unit){
        val reconnectToken = preferenceManager.getReconnectToken()
        if(reconnectToken==null){
            delay(2000)
            navigateToHome()
        }else{
            viewModel.onIntent(SplashIntent.LoadRoom(reconnectToken))
        }

    }

    LaunchedEffect(Unit){
        viewModel.events.collect{
            when(it){
                is SplashEvents.NavigateHome -> {
                    preferenceManager.deleteToken()
                    navigateToHome()
                }
                is SplashEvents.NavigatePlayRoom -> {
                    navigateToPlayRoom(it.reconnectToken)
                }
                is SplashEvents.NavigateWaitingRoom -> {
                    navigateToWaitingRoom(it.reconnectToken)
                }
            }
        }
    }




    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(Res.drawable.spades_logo),
            contentDescription = null,
        )
    }
}