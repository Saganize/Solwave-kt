package com.saganize.solwave.core.presentation.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.saganize.solwave.R

internal val interFamily = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
)

internal val rubikFamily = FontFamily(
    Font(R.font.rubik_regular, FontWeight.Normal),
)

internal val poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
)

internal val Typography = Typography(
    h3 = TextStyle(
        fontFamily = rubikFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        letterSpacing = 0.sp,
    ),
    h4 = TextStyle(
        fontFamily = rubikFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,

        letterSpacing = 1.sp,
    ),
    h5 = TextStyle(
        fontFamily = rubikFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        letterSpacing = 0.sp,
    ),
    h6 = TextStyle(
        fontFamily = rubikFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        letterSpacing = 0.15.sp,
    ),
    subtitle1 = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp,
    ),
    subtitle2 = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp,
    ),
    body1 = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.2.sp,
    ),
    body2 = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    button = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.sp,
    ),
    caption = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp,
    ),
    overline = TextStyle(
        fontFamily = interFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        letterSpacing = 1.5.sp,
    ),
)
internal val TextStyle.bold
    get() = copy(fontWeight = FontWeight.Bold)
internal val TextStyle.semiBold
    get() = copy(fontWeight = FontWeight.SemiBold)
internal val TextStyle.medium
    get() = copy(fontWeight = FontWeight.Medium)
internal val TextStyle.regular
    get() = copy(fontWeight = FontWeight.Normal)
internal val TextStyle.light
    get() = copy(fontWeight = FontWeight.Light)
internal val TextStyle.extraLight
    get() = copy(fontWeight = FontWeight.ExtraLight)
