package org.example.project.presentation.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.project.presentation.utils.RenderLottieFile

@Composable
actual fun loadingProgressBar(modifier: Modifier) {
    RenderLottieFile(
        modifier = modifier,
        file = org.example.project.R.raw.loading_dots
    )
}