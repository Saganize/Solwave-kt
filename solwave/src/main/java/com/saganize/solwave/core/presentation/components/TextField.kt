package com.saganize.solwave.core.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.saganize.solwave.R
import com.saganize.solwave.core.presentation.theme.CardBackground
import com.saganize.solwave.core.presentation.theme.SolwaveTheme
import com.saganize.solwave.core.presentation.theme.bold

@Suppress("LongParameterList")
@Composable
internal fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    textStyle: TextStyle = MaterialTheme.typography.body1,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    shape: Shape = SolwaveTheme.shapes.mediumRounded,
    contentArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    backgroundColor: Color = CardBackground,
    contentPadding: PaddingValues = PaddingValues(horizontal = SolwaveTheme.dimensions.basePadding),
    border: BorderStroke? = null,
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)
    var isFocused by remember {
        mutableStateOf(false)
    }

    val inputTextStyle = textStyle.copy(color = MaterialTheme.colors.onBackground)

    BasicTextField(
        value = textFieldValue,
        onValueChange = {
            textFieldValueState = it
            if (value != it.text) {
                onValueChange(it.text)
            }
        },
        modifier = Modifier.onFocusChanged {
            isFocused = it.isFocused
        },
        enabled = enabled,
        readOnly = readOnly,
        textStyle = inputTextStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(MaterialTheme.colors.onBackground),
        decorationBox = { innerTextField ->
            Card(
                backgroundColor = backgroundColor,
                shape = shape,
                modifier = modifier,
                border = border,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
                    horizontalArrangement = contentArrangement,
                    verticalAlignment = verticalAlignment,
                ) {
                    if (leadingIcon != null) {
                        leadingIcon()
                    }

                    if (!isFocused && value.isEmpty() && placeholder != null) {
                        placeholder()
                    } else {
                        innerTextField()
                    }

                    if (trailingIcon != null) {
                        trailingIcon()
                    }
                }
            }
        },
    )
}

@Composable
fun PinTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    textStyle: TextStyle = MaterialTheme.typography.body1,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    shape: Shape = SolwaveTheme.shapes.mediumRounded,
    backgroundColor: Color = CardBackground,
    contentSpacing: Dp = SolwaveTheme.dimensions.basePadding,
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)

    val inputTextStyle = textStyle.copy(color = MaterialTheme.colors.onBackground)

    BasicTextField(
        value = textFieldValue,
        onValueChange = {
            textFieldValueState = it
            if (value != it.text) {
                onValueChange(it.text)
            }
        },
        enabled = enabled,
        readOnly = readOnly,
        textStyle = inputTextStyle,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
        ),
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(MaterialTheme.colors.onBackground),
        decorationBox = {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                repeat(6) { index ->
                    val char = when {
                        index >= value.length -> null
                        else -> value[index]
                    }
                    Box(
                        modifier = Modifier
                            .clip(shape)
                            .background(backgroundColor)
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (char != null) {
                            AnimatedContent(targetState = visible, label = "") { isVisible ->
                                if (isVisible) {
                                    Text(text = char.toString(), fontWeight = FontWeight.Bold)
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_asterisk),
                                        contentDescription = null,
                                        tint = MaterialTheme.colors.onBackground,
                                    )
                                }
                            }
                        }
                    }
                    if (index in 0..4) {
                        Spacer(modifier = Modifier.width(contentSpacing))
                    }
                }
            }
        },
    )
}

@Preview
@Composable
fun TextFieldPreview() {
    SolwaveTheme {
        CustomTextField(
            value = "Lorem ipsum",
            onValueChange = {},
            placeholder = { },
            leadingIcon = { },
            trailingIcon = { },
            textStyle = MaterialTheme.typography.body1.bold,
            contentArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(SolwaveTheme.dimensions.basePadding)
                .height(50.dp),
        )
    }
}

@Preview
@Composable
private fun OtpTextFieldPreview() {
    SolwaveTheme {
        PinTextField(
            value = "12343",
            onValueChange = {},
            shape = MaterialTheme.shapes.medium,
            textStyle = MaterialTheme.typography.body1.bold,
            modifier = Modifier
                .padding(SolwaveTheme.dimensions.basePadding)
                .height(50.dp),
        )
    }
}
