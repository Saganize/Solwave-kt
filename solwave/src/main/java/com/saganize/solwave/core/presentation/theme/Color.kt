package com.saganize.solwave.core.presentation.theme

import androidx.compose.ui.graphics.Color

internal val NoBackground = Color(0xFFFCFCFC)
internal val BackgroundBlack = Color(0xFF111218)

internal val SaganizeBlue = Color(0xFF2380EA)
internal val AlmostBlack = Color(0xFF101010)
internal val White = Color(0xFFFFFFFF)
internal val Black = Color(0xFF000000)
internal val GrayDisabled = Color(0xFFF9F9F9)

internal val CardBackground = Color(0xFF15171E)
internal val Red100 = Color(0xFFFF6363)

internal val Green100 = Color(0xFF63FF8A)

internal val BackBlur = Color(0xB30B0C10)

internal val Color.mediumEmphasis
    get() = copy(alpha = 0.6f)

internal val Color.lowEmphasis
    get() = copy(alpha = 0.3f)
