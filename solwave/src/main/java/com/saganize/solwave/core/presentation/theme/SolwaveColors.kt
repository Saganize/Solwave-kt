package com.saganize.solwave.core.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import javax.annotation.concurrent.Immutable

@Immutable
data class SolwaveColors(
    val noBackground: Color,
    val backgroundBlack: Color,
    val saganizeBlue: Color,
    val white: Color,
    val greyDisabled: Color,
    val cardBackground: Color,
    val red100: Color,
    val green100: Color,
    val backBlur: Color,
)

fun colors(): SolwaveColors = SolwaveColors(
    noBackground = Color(0xFFFCFCFC),
    backgroundBlack = Color(0xFF111218),
    saganizeBlue = Color(0xFF2380EA),
    white = Color(0xFFFFFFFF),
    greyDisabled = Color(0xFFF9F9F9),
    cardBackground = Color(0xFF15171E),
    red100 = Color(0xFFFF6363),
    green100 = Color(0xFF63FF8A),
    backBlur = Color(0xB30B0C10),
)

internal val LocalSolwaveColors = staticCompositionLocalOf { colors() }
