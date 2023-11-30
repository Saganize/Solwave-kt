package com.saganize.solwave.solwave.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saganize.solwave.R
import com.saganize.solwave.core.models.Response
import com.saganize.solwave.core.presentation.components.AppNameBar
import com.saganize.solwave.core.presentation.theme.CardBackground
import com.saganize.solwave.core.presentation.theme.SolwaveTheme
import com.saganize.solwave.core.presentation.theme.mediumEmphasis
import com.saganize.solwave.core.presentation.theme.semiBold
import com.saganize.solwave.solwave.presentation.SolwaveViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ErrorScreen(
    viewModel: SolwaveViewModel? = null,
    title: String? = null,
) {
    val state = viewModel?.state?.value

    val errText: String = state?.error?.message ?: Response.genericErrorMsg

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(SolwaveTheme.dimensions.largePadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            AppNameBar()

            Surface(color = CardBackground, shape = CircleShape, onClick = {}) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(SolwaveTheme.dimensions.basePadding),
                )
            }
        }

        Spacer(modifier = Modifier.padding(64.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.FillBounds,
                    painter = painterResource(id = R.drawable.ic_transaction_error),
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title ?: "Something went wrong",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6.semiBold,
                color = MaterialTheme.colors.onBackground,
                fontSize = 22.sp,
            )
            Text(
                text = errText,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.onBackground.mediumEmphasis,
            )
        }

        Spacer(modifier = Modifier.padding(64.dp))
    }
}
