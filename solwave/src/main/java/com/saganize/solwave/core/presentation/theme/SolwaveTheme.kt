package com.saganize.solwave.core.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

object SolwaveTheme {

    val colors: SolwaveColors
        @ReadOnlyComposable
        @Composable
        get() = LocalSolwaveColors.current

    val dimensions: SolwaveDimensions
        @ReadOnlyComposable
        @Composable
        get() = LocalSolwaveDimensions.current

    val shapes: SolwaveShapes
        @ReadOnlyComposable
        @Composable
        get() = LocalSolwaveShapes.current
}
