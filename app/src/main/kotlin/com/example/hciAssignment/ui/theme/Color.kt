package com.example.hciAssignment.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.material3.ColorScheme

val PapayaWhip = Color(0xFFFAECCF)     // Light creamy background
val PapayaWhip2 = Color(0xFFFBEACA)    // Slightly warmer variant

val Rosewood = Color(0xFF68060D)       // Deep red-brown (focus tone)
val Garnet = Color(0xFF793B39)         // Dark reddish tone
val RoseTaupe = Color(0xFF8C5852)      // Muted rose accent
val RosewoodDark = Color(0xFF4A0008)   // Even deeper for outlines
val SoftContrast = Color(0xFFFFF6EC)   // For light-on-dark contrast text/icons

fun appLightColorScheme(): ColorScheme = ColorScheme(
    primary = Rosewood,
    onPrimary = SoftContrast,
    primaryContainer = Garnet,
    onPrimaryContainer = PapayaWhip,
    secondary = Garnet,
    onSecondary = SoftContrast,
    secondaryContainer = RoseTaupe,
    onSecondaryContainer = PapayaWhip2,
    tertiary = RoseTaupe,
    onTertiary = SoftContrast,
    tertiaryContainer = Garnet,
    onTertiaryContainer = PapayaWhip,
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = PapayaWhip,
    onBackground = Rosewood,
    surface = PapayaWhip2,
    onSurface = Rosewood,
    surfaceTint = Rosewood,
    surfaceVariant = PapayaWhip2,
    onSurfaceVariant = RosewoodDark,
    outline = RosewoodDark,
    outlineVariant = Garnet,
    scrim = Color.Black,
    inverseSurface = Garnet,
    inverseOnSurface = PapayaWhip,
    inversePrimary = Rosewood,
    surfaceDim = PapayaWhip2,
    surfaceBright = PapayaWhip,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = PapayaWhip,
    surfaceContainer = PapayaWhip2,
    surfaceContainerHigh = Garnet,
    surfaceContainerHighest = Rosewood
)

fun appDarkColorScheme(): ColorScheme = ColorScheme(
    primary = Garnet,
    onPrimary = PapayaWhip,
    primaryContainer = Rosewood,
    onPrimaryContainer = PapayaWhip2,
    secondary = RoseTaupe,
    onSecondary = PapayaWhip,
    secondaryContainer = RosewoodDark,
    onSecondaryContainer = PapayaWhip2,
    tertiary = Rosewood,
    onTertiary = PapayaWhip,
    tertiaryContainer = Garnet,
    onTertiaryContainer = PapayaWhip2,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = RosewoodDark,
    onBackground = PapayaWhip,
    surface = Rosewood,
    onSurface = PapayaWhip,
    surfaceTint = Garnet,
    surfaceVariant = Garnet,
    onSurfaceVariant = PapayaWhip2,
    outline = RoseTaupe,
    outlineVariant = Garnet,
    scrim = Color.Black,
    inverseSurface = PapayaWhip2,
    inverseOnSurface = RosewoodDark,
    inversePrimary = PapayaWhip,
    surfaceDim = Rosewood,
    surfaceBright = Garnet,
    surfaceContainerLowest = Color(0xFF1A0A06),
    surfaceContainerLow = RosewoodDark,
    surfaceContainer = Rosewood,
    surfaceContainerHigh = Garnet,
    surfaceContainerHighest = RoseTaupe
)
