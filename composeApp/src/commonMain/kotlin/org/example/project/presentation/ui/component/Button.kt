package org.example.project.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.project.presentation.ui.effects.bouncingClick
import org.example.project.presentation.ui.theme.darkPrimaryBlue

@Composable
fun buttonWithoutRipple(
    modifier: Modifier=Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    containerColor:Color=MaterialTheme.colorScheme.darkPrimaryBlue,
    contentColor:Color=Color.White,
    cornerRadius: Dp=18.dp,
    shape: RoundedCornerShape=RoundedCornerShape(cornerRadius),
    horizontalContentPadding:Dp=20.dp,
    verticalContentPadding:Dp=8.dp,
    content: @Composable () -> Unit
){

    Card(
        modifier = Modifier
            .bouncingClick(
                onClick = onClick,
                isEnable = enabled
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                color = containerColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(
                horizontal = horizontalContentPadding,
                vertical = verticalContentPadding
            )
            ,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )

    ) {

        content()


    }



}