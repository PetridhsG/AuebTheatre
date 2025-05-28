package com.example.hciAssignment.ui.screens.chatbot.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.pow

@Composable
fun TypingDots() {
    val dotCount = 3
    val durationPerDot = 150
    val bounceHeight = 6f
    val dotSize = 8.dp
    val totalCycle = durationPerDot * dotCount

    val infiniteTransition = rememberInfiniteTransition(label = "TypingDotsCycle")
    val animatedValue by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = totalCycle.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = totalCycle * 2, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "TypingDotsFloat"
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        repeat(dotCount) { index ->
            AnimatedDot(
                index = index,
                animatedValue = animatedValue,
                durationPerDot = durationPerDot,
                bounceHeight = bounceHeight,
                size = dotSize,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun AnimatedDot(
    index: Int,
    animatedValue: Float,
    durationPerDot: Int,
    bounceHeight: Float,
    size: Dp,
    color: Color
) {
    val dotStart = index * durationPerDot
    val dotEnd = dotStart + durationPerDot

    val offset = if (animatedValue in dotStart.toFloat()..dotEnd.toFloat()) {
        val progress = (animatedValue - dotStart) / durationPerDot
        -bounceHeight * (1 - (2 * progress - 1).pow(2))
    } else {
        0f
    }

    Box(
        modifier = Modifier
            .size(size)
            .offset(y = offset.dp)
            .background(color, shape = CircleShape)
    )
}
