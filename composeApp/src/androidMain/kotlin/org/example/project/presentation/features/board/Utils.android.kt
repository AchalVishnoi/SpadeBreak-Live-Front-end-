package org.example.project.presentation.features.board

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.project.R
import org.example.project.presentation.utils.RenderLottieFile
import spadebreaklive.composeapp.generated.resources.Res

@Composable
actual fun GlowingCircularBackground(modifier: Modifier) {
    RenderLottieFile(
        file = R.raw.glowing_circle,
        modifier = modifier
    )
}