package com.dogdex.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

/** Lets any screen read the user's unit preference without taking a settings dependency. */
val LocalMetricUnits = staticCompositionLocalOf { true }

private val LightColors = lightColorScheme(
    primary = DogOrange,
    onPrimary = LightSurface,
    secondary = DogOrangeDark,
    background = LightBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    onBackground = LightOnSurface,
    outline = LightOutline,
    surfaceVariant = DogOrangeSoft,
)

private val DarkColors = darkColorScheme(
    primary = DogOrange,
    onPrimary = LightOnSurface,
    secondary = DogOrangeDark,
    background = DarkBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    onBackground = DarkOnSurface,
    outline = DarkOutline,
    surfaceVariant = DarkOutline,
)

@Composable
fun DogDexTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    metricUnits: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColors else LightColors
    CompositionLocalProvider(LocalMetricUnits provides metricUnits) {
        MaterialTheme(
            colorScheme = colors,
            typography = DogDexTypography,
            content = content,
        )
    }
}
