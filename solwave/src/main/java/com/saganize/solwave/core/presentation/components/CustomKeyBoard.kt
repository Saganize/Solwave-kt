package com.saganize.solwave.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saganize.solwave.R
import com.saganize.solwave.core.presentation.theme.CardBackground
import com.saganize.solwave.core.presentation.theme.SolwaveTheme
import com.saganize.solwave.core.presentation.theme.bold

@Composable
fun CustomKeyBoard(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    fun updateValue(newValue: String) {
        if (value == "0" && newValue != ".") {
            onValueChange(newValue)
        } else {
            onValueChange(value + newValue)
        }
    }
    Card(modifier = modifier, backgroundColor = CardBackground, shape = RoundedCornerShape(18.dp)) {
        BoxWithConstraints(modifier = Modifier.wrapContentSize()) {
            val itemHeight = this.maxWidth / 5
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = SolwaveTheme.dimensions.largePadding),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    KeyBoardButton(text = "1", onClick = { updateValue("1") })
                    KeyBoardButton(text = "2", onClick = { updateValue("2") })
                    KeyBoardButton(text = "3", onClick = { updateValue("3") })
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    KeyBoardButton(text = "4", onClick = { updateValue("4") })
                    KeyBoardButton(text = "5", onClick = { updateValue("5") })
                    KeyBoardButton(text = "6", onClick = { updateValue("6") })
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    KeyBoardButton(text = "7", onClick = { updateValue("7") })
                    KeyBoardButton(text = "8", onClick = { updateValue("8") })
                    KeyBoardButton(text = "9", onClick = { updateValue("9") })
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    KeyBoardButton(text = ".", onClick = { updateValue(".") })
                    KeyBoardButton(text = "0", onClick = { updateValue("0") })
                    IconButton(onClick = {
                        if (value.length == 1) {
                            onValueChange("0")
                        } else {
                            onValueChange(value.dropLast(1))
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_backspace),
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KeyBoardButton(text: String, onClick: () -> Unit) {
    IconButton(onClick = onClick, modifier = Modifier.wrapContentSize()) {
        Text(
            text = text,
            style = MaterialTheme.typography.h5.bold,
            color = Color.White,
        )
    }
}

@Preview
@Composable
private fun CustomKeyBoardPreview() {
    CustomKeyBoard(value = "", onValueChange = {})
}
