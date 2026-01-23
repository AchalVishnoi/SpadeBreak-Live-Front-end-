package org.example.project.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.example.project.features.board.presentation.PlayingBoardScreen
import org.example.project.features.entry.presentation.HomeScreen
import org.example.project.features.entry.presentation.HomeViewModel
import org.example.project.serverRoom.SocketEngine
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NavGraph(){

    val navController = rememberNavController()
    val homeViewModel:HomeViewModel= koinViewModel()
    val socketEngine:SocketEngine= getKoin().get()


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
            HomeScreen(homeViewModel, navigateToRoom = {roomId->
                navController.navigate(NavRoutes.Room(roomId))
                }
                ,socketEngine)
        }

        composable<NavRoutes.Room> {
            val room =it.toRoute<NavRoutes.Room>()
            PlayingBoardScreen(socketEngine,room.roomId)
        }

    }


}