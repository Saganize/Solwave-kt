package com.saganize.solwave.core.events

import android.content.Context
import com.saganize.solwave.core.models.SolwaveErrors
import com.saganize.solwave.core.models.WalletProvider

sealed class SolwaveEvents {
    data class SelectWallet(
        val context: Context,
        val wallet: WalletProvider,
        val openDeepLink: () -> Unit = {},
    ) : SolwaveEvents()

    data class SaveWallet(
        val context: Context,
        val wallet: WalletProvider,
        val key: String,
    ) : SolwaveEvents()

    data class SaveWalletFromWebview(
        val wallet: WalletProvider,
        val Key: String,
        val context: Context,
    ) : SolwaveEvents()

    data class PayUsingWallet(val openDeepLink: () -> Unit = {}) : SolwaveEvents()
    data class DecryptTransactionResult(val data: String, val nonce: String) : SolwaveEvents()

    data class DecryptSignedMessageResult(
        val data: String,
        val nonce: String,
        val context: Context,
    ) : SolwaveEvents()

    data class MessageSigned(val signature: String, val messageBytes: String) : SolwaveEvents()
    data class TransactionDone(val id: String) : SolwaveEvents()
    data class Error(
        val title: String? = null,
        val error: SolwaveErrors,
        val closeWebView: Boolean = false,
    ) : SolwaveEvents()

    data class TransactionFailed(val error: SolwaveErrors) : SolwaveEvents()

    data class SignMessageUsingWallet(
        val message: String,
        val openDeepLink: () -> Unit = {},
    ) : SolwaveEvents()
}
