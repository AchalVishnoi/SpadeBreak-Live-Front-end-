package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import org.example.project.features.entry.presentation.HomeScreen
import org.example.project.presentation.features.board.presentation.PlayingBoardScreen
import org.example.project.presentation.navigation.NavGraph
import org.example.project.presentation.ui.component.lottieResourceMap
import org.example.project.presentation.ui.theme.darkPrimaryBlue
import org.example.project.presentation.ui.theme.lightPrimaryBlue
import org.example.project.presentation.utils.RenderLottieFile
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(){
    MaterialTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.lightPrimaryBlue)
                .imePadding()


        ) {
            NavGraph()
        }
    }
}