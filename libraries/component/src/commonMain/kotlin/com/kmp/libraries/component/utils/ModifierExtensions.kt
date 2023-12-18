package com.kmp.libraries.component.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset

fun Modifier.fullOffset(offset: Int) = composed {
    val density = LocalDensity.current
    val offsetPx = with(density) {
        offset.dp.roundToPx()
    }

    Modifier.layout { measurable, constraints ->
        val looseConstraints = constraints.offset(offsetPx * 2, 0)
        val placeable = measurable.measure(looseConstraints)
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }
}

fun Modifier.bounceClickable(
    scaleDown: Float = 0.95f,
    onClick: () -> Unit
) = composed {

    val interactionSource = remember { MutableInteractionSource() }

    val animatable = remember {
        Animatable(1f)
    }

    val animationSpec = SpringSpec<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessMediumLow,
    )

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> animatable.animateTo(scaleDown)
                is PressInteraction.Release -> animatable.animateTo(
                    targetValue = 1f,
                    animationSpec = animationSpec
                )

                is PressInteraction.Cancel -> animatable.animateTo(
                    targetValue = 1f,
                    animationSpec = animationSpec
                )
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