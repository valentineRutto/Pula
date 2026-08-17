package com.valentinerutto.orbmotion.orbs

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

public enum class OrbTheme {
    /** Follows [androidx.compose.foundation.isSystemInDarkTheme]. Default. */
    Auto,

    /** Pin to light-colored dots, for dark backgrounds. */
    Dark,

    /** Pin to dark-colored dots, for light backgrounds. */
    Light,
}
private val LightColors = lightColorScheme(
    primary = Color(0xFF000000),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFFFFF),
    background = Color(0xFF000000),
    surface = Color(0xFF000000)
)

@Composable
fun OrbTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors: ColorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colors, content = content)
}