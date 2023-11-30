package com.saganize.solwave.core.presentation.components

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saganize.solwave.R
import com.saganize.solwave.core.presentation.theme.GrayDisabled
import com.saganize.solwave.core.presentation.theme.White
import com.saganize.solwave.core.presentation.theme.bold
import com.saganize.solwave.core.presentation.theme.interFamily
import com.saganize.solwave.core.presentation.theme.light
import com.saganize.solwave.core.presentation.theme.mediumEmphasis

@Composable
fun AppNameBarWithClose() {
    Row(
        Modifier.fillMaxWidth().wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth(),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "Solwave",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = interFamily,
                    fontWeight = FontWeight(400),
                    color = White,
                    letterSpacing = 0.29.sp,
                ),
            )
            Spacer(modifier = Modifier.padding(1.dp))
            Text(
                text = buildAnnotatedString {
                    append("Powered by ")
                    withStyle(
                        style = MaterialTheme.typography.caption.bold.toSpanStyle(),
                    ) {
                        append("Saga")
                    }
                    withStyle(
                        style = MaterialTheme.typography.caption.toSpanStyle(),
                    ) {
                        append("nize")
                    }
                },
                style = MaterialTheme.typography.caption.light,
                color = GrayDisabled.mediumEmphasis,
            )
        }

        val activity = LocalContext.current as Activity

        IconButton(
            onClick = { activity.finish() },
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_wv_back),
                contentDescription = null,
            )
        }
    }
}
