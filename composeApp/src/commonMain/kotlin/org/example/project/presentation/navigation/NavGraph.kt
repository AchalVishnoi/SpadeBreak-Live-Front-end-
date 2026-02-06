package org.example.project.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.example.project.features.entry.presentation.HomeScreen
import org.example.project.features.entry.presentation.HomeViewModel
import org.example.project.data.remote.socket.SocketEngine
import org.example.project.presentation.features.board.presentation.PlayingBoardScreen
import org.example.project.presentation.features.splash.SplashScreen
import org.example.project.presentation.features.waitingRoom.presentation.WaitingRoomScreen
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NavGraph(){

    val navController = rememberNavController()
    val homeViewModel:HomeViewModel= koinViewModel()
    val socketEngine: SocketEngine = getKoin().get()


    NavHost(
        navController = navController,
        startDestination = NavRoutes.Splash,

        enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) }
    ) {

        composable<NavRoutes.Splash> {

            SplashScreen(
                navigateToWaitingRoom = {
                    navController.popBackStack()
                    navController.navigate(NavRoutes.WaitingRoom(it))
                },
                navigateToPlayRoom = {
                    navController.popBackStack()
                    navController.navigate(NavRoutes.WaitingRoom(it))
                    navController.navigate(NavRoutes.PlayRoom(it))
                },
                navigateToHome = {
                    navController.popBackStack()
                    navController.navigate(NavRoutes.Home)
                }
            )

        }

        composable<NavRoutes.Home> {
            HomeScreen(viewModel = homeViewModel,
                navigateToWaitingRoom = {reconnectId->
                                        navController.navigate(NavRoutes.WaitingRoom(reconnectId))
                                        },
                socketEngine=socketEngine)
        }

        composable<NavRoutes.WaitingRoom> {
            val room =it.toRoute<NavRoutes.WaitingRoom>()
            WaitingRoomScreen(
                waitingRoomViewModel = koinViewModel(),
                onBack = {
                    if(navController.previousBackStackEntry!=null){
                        navController.popBackStack()
                    }else{
                        navController.popBackStack()
                        navController.navigate(NavRoutes.Home)
                    }
                },
                navigateToPlayRoom = { reconnectToken ->
                    navController.navigate(NavRoutes.PlayRoom(reconnectToken))
                },
                reconnectToken = room.reconnectId
            )
        }

        composable<NavRoutes.PlayRoom> {
            val room =it.toRoute<NavRoutes.PlayRoom>()
            PlayingBoardScreen(
                room.reconnectId,
                navigateBack ={
                    navController.popBackStack()
                },
                navigateToHomeScreen = {
                    navController.popBackStack()
                    navController.navigate(NavRoutes.Home)
                }
            )
        }



    }


}