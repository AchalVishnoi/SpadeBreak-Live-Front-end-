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
        startDestination = NavRoutes.Home,

        enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(300)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(300)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) }
    ) {

        composable<NavRoutes.Splash> {

        }

        composable<NavRoutes.Home> {
            HomeScreen(viewModel = homeViewModel,
                navigateToWaitingRoom = {roomId,playerId->
                                        navController.popBackStack()
                                        navController.navigate(NavRoutes.WaitingRoom(roomId,playerId))
                                        },
                socketEngine=socketEngine)
        }

        composable<NavRoutes.WaitingRoom> {
            val room =it.toRoute<NavRoutes.WaitingRoom>()
            WaitingRoomScreen(
                waitingRoomViewModel = koinViewModel(),
                roomId = room.roomId,
                playerId = room.playerId,
                navigateToPlayRoom = {roomId,playerId->
                    navController.popBackStack()
                    navController.navigate(NavRoutes.PlayRoom(roomId,playerId))
                }
            )
        }

        composable<NavRoutes.PlayRoom> {
            val room =it.toRoute<NavRoutes.PlayRoom>()
            PlayingBoardScreen(room.roomId)
        }



    }


}