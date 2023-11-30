package com.saganize.solwave.core.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import javax.annotation.concurrent.Immutable

@Immutable
data class SolwaveShapes(
    val smallRounded: Shape,
    val mediumRounded: Shape,
    val largeRounded: Shape,
)

fun defaultShapes() = SolwaveShapes(
    smallRounded = RoundedCornerShape(4.dp),
    mediumRounded = RoundedCornerShape(8.dp),
    largeRounded = RoundedCornerShape(16.dp),
)

internal val LocalSolwaveShapes = staticCompositionLocalOf { defaultShapes() }
