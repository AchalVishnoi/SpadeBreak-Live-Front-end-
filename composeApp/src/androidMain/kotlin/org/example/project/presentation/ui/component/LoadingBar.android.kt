package org.example.project.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.example.project.presentation.ui.theme.lightPrimaryBlue
import org.example.project.presentation.utils.RenderLottieFile
import org.jetbrains.compose.resources.painterResource
import spadebreaklive.composeapp.generated.resources.Res
import spadebreaklive.composeapp.generated.resources.spades_logo

@Composable
actual fun loadingProgressBar(modifier: Modifier) {

    println("loading prograss bar called")

            RenderLottieFile(
                modifier = modifier,
                file = org.example.project.R.raw.circular_loader
            )





}