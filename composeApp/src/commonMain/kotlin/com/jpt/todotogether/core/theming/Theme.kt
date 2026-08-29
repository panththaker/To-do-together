package com.jpt.todotogether.core.theming

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define custom light colors
val LightAppColors = lightColorScheme(
    primary = Color(0xFF4DA8DA),
    secondary = Color(0xFF4DA8DA),
    background = Color(0xFFF7F8FC),
    primaryContainer = Color(0x1F4DA8DA),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1A1A2E),
    onSurface = Color(0xFF1A1A2E),
    surfaceDim = Color(0x141A1A2E),
    // ... other colors
)


@Composable
fun Theme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Detect system dark theme preference
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        LightAppColors //DarkAppColors
    } else {
        LightAppColors
    }

    MaterialTheme(
        colorScheme = colors,
        // ... other theme parameters like typography and shapes
        content = content
    )
}