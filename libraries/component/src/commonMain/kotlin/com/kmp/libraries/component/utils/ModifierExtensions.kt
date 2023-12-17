package com.kmp.libraries.component.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer

//fun Modifier.bounceClickable(
//    enabled: Boolean = true,
//    onClick: () -> Unit
//) = composed {
//    val interactionSource = remember { MutableInteractionSource() }
//    val isPressed by interactionSource.collectIsPressedAsState()
//
//    val animationTransition = updateTransition(isPressed, label = "BouncingClickableTransition")
//    val scaleFactor by animationTransition.animateFloat(
//        targetValueByState = { pressed -> if (pressed) 0.95f else 1f },
//        label = "BouncingClickableScaleFactorTransition",
//    )
//    val opacity by animationTransition.animateFloat(
//        targetValueByState = { pressed -> if (pressed) 0.7f else 1f },
//        label = "BouncingClickableOpacityTransition"
//    )
//
//    this
//        .graphicsLayer {
//            this.scaleX = scaleFactor
//            this.scaleY = scaleFactor
//            this.alpha = opacity
//        }
//        .clickable(
//            interactionSource = interactionSource,
//            indication = null,
//            enabled = enabled,
//            onClick = onClick
//        )
//}

fun Modifier.bounceClickable(
    scaleDown: Float = 0.95f,
    onClick: () -> Unit
) = composed {

    val interactionSource = remember { MutableInteractionSource() }

    val animatable = remember {
        Animatable(1f)
    }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> animatable.animateTo(scaleDown)
                is PressInteraction.Release -> animatable.animateTo(1f)
                is PressInteraction.Cancel -> animatable.animateTo(1f)
            }
        }
    }

    Modifier
        .graphicsLayer {
            val scale = animatable.value
            scaleX = scale
            scaleY = scale
        }
        .clickable(
            interactionSource = interactionSource,
            indication = null
        ) {
            onClick()
        }
}