package org.example.project.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.zIndex

@Composable
expect fun loadingProgressBar(modifier: Modifier)

@Composable
fun FullScreenBlurredBackgroundLoader(
    backgroundColor: Color = Color.Black.copy(alpha = 0.5f),
    loaderContent: @Composable () -> Unit = {
        loadingProgressBar(modifier = Modifier.size(100.dp))
    }
) {

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        keyboardController?.hide()
    }

    Box( modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor)
        .zIndex(1f)
        .pointerInput(Unit) {detectTapGestures { }},
        contentAlignment = Alignment.Center){

        loaderContent()



    }

}


@Composable
fun FullScreenLoader( loaderContent: @Composable () -> Unit = {
    loadingProgressBar(modifier = Modifier.size(100.dp))
}){

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        loaderContent()

    }
}