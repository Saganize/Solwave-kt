package com.saganize.solwave.solwave.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saganize.solwave.R
import com.saganize.solwave.core.events.SolwaveEvents
import com.saganize.solwave.core.models.WalletProvider
import com.saganize.solwave.core.presentation.components.AppNameBarWithClose
import com.saganize.solwave.core.presentation.theme.CardBackground
import com.saganize.solwave.core.presentation.theme.SaganizeBlue
import com.saganize.solwave.core.presentation.theme.SolwaveTheme
import com.saganize.solwave.core.presentation.theme.bold
import com.saganize.solwave.core.presentation.theme.medium
import com.saganize.solwave.core.presentation.theme.mediumEmphasis
import com.saganize.solwave.core.presentation.theme.semiBold
import com.saganize.solwave.core.util.extensions.displayWallet
import com.saganize.solwave.solwave.presentation.SolwaveViewModel
import com.saganize.solwave.solwave.presentation.components.WalletItem
import com.saganize.solwave.solwave.presentation.components.externalWalletLauncher

@Composable
fun WalletScreen(
    viewModel: SolwaveViewModel? = null,
) {
//    BackHandler(enabled = true) { }

    val state = viewModel?.state?.value
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(SolwaveTheme.dimensions.largePadding),
    ) {
        AppNameBarWithClose()

        Spacer(modifier = Modifier.padding(12.dp))

        Text(
            text = "RECOMMENDED",
            style = MaterialTheme.typography.overline.semiBold,
            color = SolwaveTheme.colors.greyDisabled.mediumEmphasis,
        )

        Spacer(modifier = Modifier.height(SolwaveTheme.dimensions.basePadding))

        Card(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = CardBackground,
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        modifier = Modifier.wrapContentWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            SolwaveTheme.dimensions.basePadding,
                        ),
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(22.dp)
                                .padding(2.dp),
                            painter = painterResource(id = R.drawable.ic_saga),
                            contentDescription = null,
                            tint = Color.Unspecified,
                        )

                        Text(
                            text = buildAnnotatedString {
                                append("Saga")
                                withStyle(
                                    style = MaterialTheme.typography.body1.medium.toSpanStyle(),
                                ) {
                                    append("nize")
                                }
                            },
                            color = Color.White,
                            style = MaterialTheme.typography.body1.bold,
                        )
                    }

                    TextButton(onClick = {
                        viewModel?.onEvent(
                            SolwaveEvents.SelectWallet(
                                context,
                                WalletProvider.Saganize,
                            ),
                        )
                    }) {
                        if (state?.wallet?.walletProvider == WalletProvider.Saganize) {
                            Text(
                                text = "SELECTED",
                                style = MaterialTheme.typography.overline.semiBold,
                                color = SaganizeBlue,
                            )
                        } else {
                            Text(
                                text = "SELECT",
                                color = MaterialTheme.colors.onBackground,
                                style = MaterialTheme.typography.overline.medium,
                            )
                        }
                    }
                }

                AnimatedVisibility(
                    visible = state?.wallet?.walletProvider == WalletProvider.Saganize,
                ) {
                    state?.wallet?.key?.displayWallet()?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.caption,
                            color = SolwaveTheme.colors.greyDisabled.mediumEmphasis,
                            fontSize = 10.sp,
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.padding(12.dp))

        Text(
            text = "OTHER WALLETS",
            style = MaterialTheme.typography.overline.semiBold,
            color = SolwaveTheme.colors.greyDisabled.mediumEmphasis,
        )

        Spacer(modifier = Modifier.padding(SolwaveTheme.dimensions.basePadding))

        val keypair = state?.keyPair ?: return
        val phantomOpen = externalWalletLauncher(
            stringResource(R.string.phantom_app_connect),
            keypair.first,
        )

        val solflareOpen = externalWalletLauncher(
            stringResource(R.string.solflare_app_connect),
            keypair.first,
        )

        Card(backgroundColor = CardBackground, modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(12.dp),
            ) {
                WalletItem(
                    isSelected = state.wallet?.walletProvider == WalletProvider.Phantom,
                    iconPainter = painterResource(id = R.drawable.ic_phantom),
                    name = stringResource(R.string.phantom_app_name),
                    key = state.wallet?.key,
                    walletPackage = stringResource(R.string.phantom_wallet_package),
                    onConnectClick = {
                        viewModel.onEvent(
                            SolwaveEvents.SelectWallet(
                                context,
                                WalletProvider.Phantom,
                                phantomOpen,
                            ),
                        )
                    },
                )

                WalletItem(
                    isSelected = state.wallet?.walletProvider == WalletProvider.Solflare,
                    iconPainter = painterResource(id = R.drawable.ic_solflare),
                    name = stringResource(R.string.solflare_app_name),
                    key = state.wallet?.key,
                    walletPackage = stringResource(R.string.solflare_wallet_package),
                    onConnectClick = {
                        viewModel.onEvent(
                            SolwaveEvents.SelectWallet(
                                context,
                                WalletProvider.Solflare,
                                solflareOpen,
                            ),
                        )
                    },
                )
            }
        }

        Spacer(modifier = Modifier.padding(66.dp))
    }
}

@Preview
@Composable
private fun ChooseWalletPreview() {
    SolwaveTheme {
        WalletScreen(viewModel = null)
    }
}
