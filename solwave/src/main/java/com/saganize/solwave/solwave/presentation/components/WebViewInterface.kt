package com.saganize.solwave.solwave.presentation.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import com.google.gson.GsonBuilder
import com.saganize.solwave.core.events.SolwaveEvents
import com.saganize.solwave.core.models.Response.Companion.genericErrorMsg
import com.saganize.solwave.core.util.extensions.defaultStringify
import com.saganize.solwave.domain.model.TransactionParams
import com.saganize.solwave.domain.model.TransactionParamsStringHolder
import com.saganize.solwave.domain.model.TransactionPayloadStringHolder
import com.saganize.solwave.domain.model.TransactionSerializer
import com.saganize.solwave.solwave.presentation.SolwaveViewModel
import com.solana.core.Transaction

class WebViewInterface(
    private val viewmodel: SolwaveViewModel,
    private val context: Context,
    private val transactionParams: TransactionParams,
    private val onWalletReceived: (emailId: String, publicKey: String) -> Unit,
    private val onToast: (message: String, shouldEndWebView: Boolean) -> Unit = { _, _ -> },
    private val onClosed: (success: Long, failure: String) -> Unit,
) {
    @JavascriptInterface
    fun getEmailAndPublicKey(emailId: String, publicKey: String) {
        Log.d("WebViewInterface", "getEmailAndPublicKey: $emailId, $publicKey")
        onWalletReceived(emailId, publicKey)
    }

    @JavascriptInterface
    fun copyPublicKey(publicKey: String) {
        Log.d("WebViewInterface", "copyPublicKey: $publicKey")
        val clipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        val clip = ClipData.newPlainText("Public key", publicKey)
        clipboardManager?.setPrimaryClip(clip) ?: Log.e(
            "Clipboard",
            "Failed to access the clipboard.",
        )
    }

    @JavascriptInterface
    fun showToast(
        message: String = genericErrorMsg,
        shouldEndWebView: Boolean = false,
    ) {
        Log.d("WebViewInterface", "showToast: $message")
        onToast(message, shouldEndWebView)
    }

    @JavascriptInterface
    fun getTransaction(): String {
        Log.d("WebViewInterface", "getTransaction: ${transactionParams.data.transaction}")

        val params = TransactionParamsStringHolder(
            type = transactionParams.type,
            status = transactionParams.status,
            data = TransactionPayloadStringHolder(
                transaction = transactionParams.data.transaction.defaultStringify(),
                from = transactionParams.data.from,
                to = transactionParams.data.to,
                lamports = transactionParams.data.lamports,
                fees = transactionParams.data.fees,
            ),
        )

        val gsonBuilder = GsonBuilder()
        val gson = gsonBuilder.create()
        gsonBuilder.registerTypeAdapter(Transaction::class.java, TransactionSerializer())
        val gsonString = gson.toJson(params)
        Log.d("WebViewInterface", "getTransaction: gson: $gsonString")

        return gsonString
    }

    @JavascriptInterface
    fun transactionComplete(signature: String, idempotencyId: String) {
        Log.d(
            "WebViewInterface",
            "transactionCompleted: signature: $signature, idempotencyId: $idempotencyId",
        )
        viewmodel.onEvent(SolwaveEvents.TransactionDone(signature))
    }

    @JavascriptInterface
    fun close(event: Long, message: String) {
        Log.d(
            "WebViewInterface",
            "close: WebView closed, event: ${getWebViewClosingEvent(event).name}, message: $message",
        )
        onClosed(event, message)
    }
}

enum class WebViewClosingEvents {
    UserCreationSuccess,
    LoginSuccessful,
    TransactionCompleted,
    UserCreationFailure,
    LoginFailure,
    ServerError,
    TransactionFailed,
}

fun getWebViewClosingEvent(ordinal: Long): WebViewClosingEvents {
    return when (ordinal) {
        0L -> WebViewClosingEvents.UserCreationSuccess
        1L -> WebViewClosingEvents.LoginSuccessful
        2L -> WebViewClosingEvents.TransactionCompleted
        3L -> WebViewClosingEvents.UserCreationFailure
        4L -> WebViewClosingEvents.LoginFailure
        5L -> WebViewClosingEvents.ServerError
        6L -> WebViewClosingEvents.TransactionFailed
        else -> WebViewClosingEvents.ServerError
    }
}
