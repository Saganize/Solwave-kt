package com.saganize.solwave.solwave.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saganize.solwave.R
import com.saganize.solwave.core.events.SolwaveNavEvents
import com.saganize.solwave.core.presentation.components.AppNameBar
import com.saganize.solwave.core.presentation.components.ButtonPrimary
import com.saganize.solwave.core.presentation.theme.Dimensions
import com.saganize.solwave.core.presentation.theme.SolwaveTheme
import com.saganize.solwave.core.presentation.theme.bold
import com.saganize.solwave.core.presentation.theme.interFamily
import com.saganize.solwave.core.presentation.theme.lowEmphasis
import com.saganize.solwave.core.presentation.theme.poppins
import com.saganize.solwave.core.presentation.theme.rubikFamily
import com.saganize.solwave.core.util.extensions.copyToClipboard
import com.saganize.solwave.core.util.extensions.displayWallet
import com.saganize.solwave.core.util.extensions.getPublicKey
import com.saganize.solwave.core.util.extensions.showToast
import com.saganize.solwave.core.util.extensions.toSol
import com.saganize.solwave.solwave.presentation.SolwaveViewModel
import kotlinx.coroutines.delay

@Composable
fun AddFundsScreen(viewModel: SolwaveViewModel? = null) {
    val state = viewModel?.state?.value
    val currentWallet = state?.wallet ?: return

    val context = LocalContext.current

    val publicKey = currentWallet.key.getPublicKey()

    var btnLoading by remember { mutableStateOf(false) }
    var showTimer by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableIntStateOf(0) }

    val delayTimes = listOf(3, 5, 7, 10) // Delays in seconds
    var currentDelayIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(showTimer) {
        if (showTimer) {
            // Calculate the next delay time, defaulting to 10 seconds if we exceed the predefined times
            val delayTime = delayTimes.getOrNull(currentDelayIndex) ?: 10
            timeRemaining = delayTime
            while (timeRemaining > 0) {
                delay(1_000L)
                timeRemaining--
            }

            showTimer = false
            btnLoading = false
            currentDelayIndex = (currentDelayIndex + 1).coerceAtMost(delayTimes.lastIndex)
        }
    }

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(SolwaveTheme.dimensions.largePadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AppNameBar()

        Image(
            modifier = Modifier.padding(
                top = SolwaveTheme.dimensions.mediumPadding,
                bottom = SolwaveTheme.dimensions.largePadding,
            ),
            painter = painterResource(id = R.drawable.no_funds),
            contentDescription = null,
        )

        Text(
            text = "Add funds to wallet",
            style = TextStyle(
                fontSize = 28.sp,
                lineHeight = 30.sp,
                fontFamily = rubikFamily,
                fontWeight = FontWeight(600),
                color = Color(0xFFF9F9F9),
                textAlign = TextAlign.Center,
            ),
        )

        Spacer(modifier = Modifier.height(SolwaveTheme.dimensions.mediumPadding))

        Text(
            text = "Your wallet is low on balance. Add some funds and try again later.",
            style = TextStyle(
                fontSize = 13.sp,
                lineHeight = 18.sp,
                fontFamily = poppins,
                fontWeight = FontWeight(300),
                color = Color(0xBFFFFFFF),
                textAlign = TextAlign.Center,
            ),
        )

        Box(
            modifier = Modifier
                .padding(top = SolwaveTheme.dimensions.mediumPadding, bottom = 40.dp)
                .height(60.dp)
                .wrapContentWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                modifier = Modifier
                    .border(
                        border = BorderStroke(
                            width = 0.5.dp,
                            color = MaterialTheme.colors.onBackground.lowEmphasis,
                        ),
                        shape = RoundedCornerShape(100),
                    )
                    .clip(RoundedCornerShape(100))
                    .clickable {
                        context.copyToClipboard(
                            "Wallet Address",
                            publicKey,
                        ) {
                            context.showToast("Address copied to clipboard")
                        }
                    }
                    .padding(vertical = Dimensions.BasePadding.dp)
                    .padding(
                        start = Dimensions.LargePadding.dp,
                        end = Dimensions.MediumPadding.dp,
                    )
                    .wrapContentWidth(Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = publicKey.displayWallet(),
                    style = MaterialTheme.typography.body2.bold.copy(
                        color = MaterialTheme.colors.onBackground,
                        fontFamily = poppins,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier.align(Alignment.CenterVertically),
                )

                Spacer(modifier = Modifier.width(Dimensions.MediumPadding.dp))

                Icon(
                    painter = painterResource(id = R.drawable.copy),
                    contentDescription = "copy icon",
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier
                        .size(SolwaveTheme.dimensions.largePadding)
                        .align(Alignment.CenterVertically),
                )
            }
        }

        val amount = state.transactionParams.data.lamports?.toSol() ?: "0"

        Text(
            text = "You need $amount Sol for the transaction",
            style = TextStyle(
                fontSize = 12.sp,
                lineHeight = 14.sp,
                fontFamily = interFamily,
                fontWeight = FontWeight(400),
                color = Color(0xFFF9F9F9),
                textAlign = TextAlign.Center,
            ),
        )

        Spacer(modifier = Modifier.size(SolwaveTheme.dimensions.basePadding))

        ButtonPrimary(
            onClick = {
                btnLoading = true
                viewModel.getBalance()

                val totalPayAmount = if (state.transactionParams.data.lamports != null) {
                    state.transactionParams.data.lamports + state.transactionParams.data.fees
                } else {
                    state.transactionParams.data.fees
                }

                Log.d(
                    "AddFundsScreen",
                    "totalPayAmount: $totalPayAmount, lamports: ${state.transactionParams.data.lamports}," +
                        " fees: ${state.transactionParams.data.fees}",
                )
                val isSufficientFundsAvailable = if (state.balance != null) {
                    state.balance >= totalPayAmount
                } else {
                    false
                }

                showTimer = if (isSufficientFundsAvailable) {
                    viewModel.onNav(SolwaveNavEvents.GoToPayScreen)
                    false
                } else {
                    context.showToast(
                        "Funds not available, please try again after some time.",
                        Toast.LENGTH_SHORT,
                    )
                    true
                }
                btnLoading = false
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !btnLoading && !showTimer,
        ) {
            if (btnLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(SolwaveTheme.dimensions.largePadding)
                        .padding(4.dp),
                    color = Color.Black,
                )
            } else {
                Text(
                    text = if (showTimer) "Continue in ($timeRemaining)" else "Continue",
                    style = MaterialTheme.typography.button.bold,
                    color = MaterialTheme.colors.onBackground,
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
