package com.saganize.solwave.solwave.presentation.components

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.saganize.solwave.R

@Composable
fun externalWalletLauncher(walletUrl: String, publicEncryptionKey: String): () -> Unit {
    val deeplink = walletUrl +
        stringResource(R.string.saganize_url) +
        stringResource(R.string.dapp_encryption_public_key) + publicEncryptionKey +
        stringResource(R.string.cluster_devnet) +
        stringResource(R.string.redirect_link)

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(deeplink))

    val activityResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) {
    }

    return { activityResultLauncher.launch(intent) }
}

@Composable
fun solanaWalletLauncherPayment(deeplink: String): () -> Unit {
    val intent = Intent(Intent.ACTION_VIEW, deeplink.let { Uri.parse(it) })

    val activityResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) {
    }

    return { activityResultLauncher.launch(intent) }
}
