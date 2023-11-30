package com.saganize.solwave.core.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = SaganizeBlue,
    primaryVariant = SaganizeBlue,
    secondary = SaganizeBlue,
    background = Color.Red,
    surface = NoBackground,
)

@Composable
internal fun SolwaveTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content,
    )
}
