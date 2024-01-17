package com.saganize.solwave.solwave.presentation.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.saganize.solwave.core.presentation.theme.GrayDisabled
import com.saganize.solwave.core.presentation.theme.SaganizeBlue
import com.saganize.solwave.core.presentation.theme.SolwaveTheme
import com.saganize.solwave.core.presentation.theme.medium
import com.saganize.solwave.core.presentation.theme.mediumEmphasis
import com.saganize.solwave.core.presentation.theme.semiBold
import com.saganize.solwave.core.util.extensions.displayWallet
import com.saganize.solwave.core.util.extensions.getPublicKey
import com.saganize.solwave.core.util.extensions.isAppInstalled
import com.saganize.solwave.core.util.extensions.showToast

@Composable
fun WalletItem(
    isSelected: Boolean,
    iconPainter: Painter,
    name: String,
    key: String?,
    walletPackage: String,
    modifier: Modifier = Modifier,
    onConnectClick: () -> Unit,
) {
    val context = LocalContext.current

    Column {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SolwaveTheme.dimensions.basePadding),
        ) {
            Icon(
                painter = iconPainter,
                contentDescription = null,
                tint = Color.Unspecified,
            )
            Text(
                modifier = Modifier.weight(1f),
                text = name,
                color = Color.White,
                style = MaterialTheme.typography.body1.semiBold,
            )

            TextButton(
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = Color.White,
                ),
                onClick = {
                    if (context.isAppInstalled(walletPackage)) {
                        onConnectClick()
                    } else {
                        context.showToast("You do not have $name installed", Toast.LENGTH_SHORT)
                    }
                },
            ) {
                if (isSelected) {
                    Text(
                        text = "SELECTED",
                        style = MaterialTheme.typography.overline.semiBold,
                        color = SaganizeBlue,
                    )
                } else {
                    Text(
                        text = "SELECT",
                        color = if (context.isAppInstalled(walletPackage)) MaterialTheme.colors.onBackground else Color.Gray,
                        style = MaterialTheme.typography.overline.medium,
                    )
                }
            }
        }

        key?.let {
            AnimatedVisibility(visible = isSelected) {
                Text(
                    text = it.getPublicKey().displayWallet(),
                    style = MaterialTheme.typography.caption,
                    color = GrayDisabled.mediumEmphasis,
                    fontSize = 10.sp,
                )
            }
        }
    }
}
