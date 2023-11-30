package com.saganize.solwave.solwave.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.saganize.solwave.R
import com.saganize.solwave.core.presentation.components.AppNameBar
import com.saganize.solwave.core.presentation.theme.SolwaveTheme

@Composable
fun LoadingItem() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.loading_animation,
        ),
    )

    val iterations by remember {
        mutableIntStateOf(LottieConstants.IterateForever)
    }

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations,
    )

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(SolwaveTheme.dimensions.largePadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppNameBar()

        Spacer(modifier = Modifier.padding(SolwaveTheme.dimensions.largePadding))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.FillBounds,

            )
        }

        Spacer(modifier = Modifier.padding(50.dp))
    }

    Spacer(
        modifier = Modifier
            .height(100.dp)
            .background(Color.Transparent),
    )
}
