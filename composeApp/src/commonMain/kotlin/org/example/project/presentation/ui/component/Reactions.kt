package org.example.project.presentation.ui.component

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.example.project.presentation.ui.effects.bouncingClick
import org.example.project.presentation.utils.RenderLottieFile


expect val lottieResourceMap : Map<String,Int>

@Composable
private fun ReactionEmojiButton(modifier: Modifier,int: Int,onClick: () -> Unit){

    IconButton(
        onClick = {},
        modifier = modifier
    ){

        RenderLottieFile(modifier = modifier, file = int)

    }

}