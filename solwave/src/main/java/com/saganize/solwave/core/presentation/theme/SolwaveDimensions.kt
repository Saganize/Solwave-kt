package com.saganize.solwave.core.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class SolwaveDimensions(
    val basePadding: Dp,
    val mediumPadding: Dp,
    val largePadding: Dp,
    val buttonSize: Dp,
)

fun defaultDimensions(): SolwaveDimensions = SolwaveDimensions(
    basePadding = 8.dp,
    mediumPadding = 16.dp,
    largePadding = 24.dp,
    buttonSize = 48.dp,
)

internal val LocalSolwaveDimensions = staticCompositionLocalOf { defaultDimensions() }
