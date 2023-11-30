package com.saganize.solwave.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.saganize.solwave.core.presentation.theme.Dimensions
import com.saganize.solwave.core.presentation.theme.mediumEmphasis

@Composable
fun ButtonPrimary(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(percent = 100),
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onBackground,
        disabledBackgroundColor = MaterialTheme.colors.primary.mediumEmphasis,
    ),
    content: @Composable (RowScope) -> Unit,
) {
    Button(
        modifier = modifier.requiredHeight(Dimensions.ButtonSize.dp),
        onClick = onClick,
        enabled = enabled,
        elevation = ButtonDefaults.elevation(0.dp, 0.dp),
        shape = shape,
        border = border,
        colors = colors,
        content = content,
    )
}
