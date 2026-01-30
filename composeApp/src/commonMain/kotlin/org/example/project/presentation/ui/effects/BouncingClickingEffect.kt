package org.example.project.presentation.ui.effects

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import org.example.project.presentation.utils.SoundPlayer
import org.example.project.presentation.utils.UiSound
import org.koin.compose.getKoin

fun Modifier.bouncingClick(
    onClick: () -> Unit,
    isEnable: Boolean = true):Modifier = composed{

        var soundPlayer: SoundPlayer = getKoin().get()

        var isPressed by remember { mutableStateOf(false) }
        val scale by animateFloatAsState(
            targetValue = if (isPressed) 0.95f else 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        this.graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
            .pointerInput(Unit) {
                if (isEnable) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true
                            tryAwaitRelease()
                            isPressed = false
                        },
                        onTap = {
                            soundPlayer.playSound(UiSound.CLICK)
                            onClick()
                        }
                    )
                }
            }
    }

