package com.example.hciAssignment.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
class CustomColors(
    val userMessageText: Color,
    val botMessageText: Color,
    val userMessageBackground: Color,
    val botMessageBackground: Color,
)

fun lightCustomColors() = CustomColors(
    userMessageBackground = Rosewood,
    botMessageBackground = Garnet,
    userMessageText = PapayaWhip,
    botMessageText = SoftContrast
)

fun darkCustomColors() = CustomColors(
    userMessageBackground = Garnet,
    botMessageBackground = Rosewood,
    userMessageText = PapayaWhip,
    botMessageText = PapayaWhip2
)

val LocalCustomColors = staticCompositionLocalOf<CustomColors> {
    error("No CustomColors provided")
}

val customColors: CustomColors
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomColors.current
