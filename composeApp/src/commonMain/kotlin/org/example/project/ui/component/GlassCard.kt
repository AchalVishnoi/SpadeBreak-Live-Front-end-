package org.example.project.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.project.ui.theme.lightPrimaryBlue


@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp=20.dp,
    blurRadius: Dp=16.dp,
    tint:Color= MaterialTheme.colorScheme.lightPrimaryBlue.copy(alpha = 0.3f),
    content : @Composable ()-> Unit
    ) {

    Box(modifier=Modifier
        .clip(RoundedCornerShape(cornerRadius))){

        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(blurRadius)
                .background(tint)
                .border(width = 1.dp, color = Color.White)

        )

        content()

    }



}