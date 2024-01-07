package com.saganize.solwave.solwave.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saganize.solwave.core.events.SolwaveAuthNavEvents
import com.saganize.solwave.core.events.SolwaveEvents
import com.saganize.solwave.core.models.DeeplinkActionType
import com.saganize.solwave.core.models.WalletProvider
import com.saganize.solwave.core.presentation.components.AppNameBar
import com.saganize.solwave.core.presentation.components.ButtonPrimary
import com.saganize.solwave.core.presentation.theme.SolwaveTheme
import com.saganize.solwave.core.presentation.theme.bold
import com.saganize.solwave.core.presentation.theme.lowEmphasis
import com.saganize.solwave.core.presentation.theme.medium
import com.saganize.solwave.core.presentation.theme.mediumEmphasis
import com.saganize.solwave.core.util.extensions.copyToClipboard
import com.saganize.solwave.core.util.extensions.displayWallet
import com.saganize.solwave.core.util.extensions.getPublicKey
import com.saganize.solwave.core.util.extensions.showToast
import com.saganize.solwave.solwave.model.SolwaveState
import com.saganize.solwave.solwave.presentation.SolwaveViewModel
import com.saganize.solwave.solwave.presentation.components.solanaWalletLauncherPayment

@Composable
fun SignMessageScreen(
    message: String,
    viewModel: SolwaveViewModel? = null,
) {
    val state = viewModel?.state?.value
    val currentWallet = state?.wallet ?: return

    val context = LocalContext.current

    val paymentEvent = state.deepLink?.let { solanaWalletLauncherPayment(it) }

    LaunchedEffect(state.deepLink) {
        viewModel.updateDeeplinkActionType(DeeplinkActionType.SIGN_MESSAGE)
        paymentEvent?.invoke()
    }

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(SolwaveTheme.dimensions.largePadding),
    ) {
        AppNameBar()

        Spacer(modifier = Modifier.padding(6.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = SolwaveTheme.colors.cardBackground,
            border = BorderStroke(
                width = 0.5.dp,
                color = MaterialTheme.colors.onBackground.lowEmphasis,
            ),
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
            ) {
                Text(
                    text = "Message: ",
                    style = MaterialTheme.typography.body2,
                    color = SolwaveTheme.colors.white,
                )

                Spacer(modifier = Modifier.padding(6.dp))

                Text(
                    text = message.take(SolwaveState.MAX_SIGN_MESSAGE_LENGTH),
                    style = MaterialTheme.typography.body2,
                    color = SolwaveTheme.colors.greyDisabled.mediumEmphasis,
                )
            }
        }

        Spacer(modifier = Modifier.padding(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = SolwaveTheme.colors.cardBackground,
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
            ) {
                Text(
                    text = "sign using",
                    style = MaterialTheme.typography.caption,
                    color = SolwaveTheme.colors.greyDisabled.mediumEmphasis,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    when (currentWallet.walletProvider) {
                        WalletProvider.Saganize -> Text(
                            text = buildAnnotatedString {
                                append("Saga")
                                withStyle(
                                    style = MaterialTheme.typography.body1.medium.toSpanStyle(),
                                ) {
                                    append("nize")
                                }
                            },
                            style = MaterialTheme.typography.body1.bold,
                            color = Color.White,
                        )

                        WalletProvider.Phantom -> Text(
                            text = "Phantom",
                            style = MaterialTheme.typography.body1.bold,
                            color = Color.White,
                        )

                        WalletProvider.Solflare -> Text(
                            text = "Solflare",
                            style = MaterialTheme.typography.body1.bold,
                            color = Color.White,
                        )
                    }
                }

                Text(
                    modifier = Modifier.clickable {
                        context.copyToClipboard(
                            "Wallet Address",
                            currentWallet.key.getPublicKey(),
                        ) {
                            context.showToast("Address copied to clipboard")
                        }
                    },
                    text = currentWallet.key.displayWallet(),
                    style = MaterialTheme.typography.caption,
                    color = SolwaveTheme.colors.greyDisabled.mediumEmphasis,
                    fontSize = 10.sp,
                )
            }
        }

        Spacer(modifier = Modifier.padding(SolwaveTheme.dimensions.mediumPadding))

        // TODO: add loading indicator
        ButtonPrimary(
            onClick = {
                if (currentWallet.walletProvider == WalletProvider.Saganize) {
                    viewModel.onAuthEvent(
                        SolwaveAuthNavEvents.InitiateSignMessage(
                            context,
                            currentWallet.key,
                        ),
                    )
                } else {
                    viewModel.onEvent(
                        SolwaveEvents.SignMessageUsingWallet(message),
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Sign Message",
                style = MaterialTheme.typography.button.bold,
                color = MaterialTheme.colors.onBackground,
            )
        }
    }
}

@Composable
@Preview
fun SignMessageScreenPreview() {
    val message = "To avoid digital dognappers, sign below to authenticate with CryptoCorgis"
    SolwaveTheme {
        SignMessageScreen(
            message = message,
        )
    }
}
