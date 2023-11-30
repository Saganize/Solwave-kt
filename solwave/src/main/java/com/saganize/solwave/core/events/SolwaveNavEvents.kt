package com.saganize.solwave.core.events

import android.content.Context

sealed class SolwaveNavEvents {
    object GoToLoadingScreen : SolwaveNavEvents()
    object GoToLoginScreen : SolwaveNavEvents()
    object GoToNoAccountScreen : SolwaveNavEvents()
    object GoToSignupScreen : SolwaveNavEvents()
    object GoToWalletScreen : SolwaveNavEvents()
    object GoToPayScreen : SolwaveNavEvents()
    object GoToNoFundsScreen : SolwaveNavEvents()
    data class CloseWebViewScreen(
        val context: Context,
        val closeActivity: Boolean = false,
    ) : SolwaveNavEvents()

    object GoToTransactionDoneScreen : SolwaveNavEvents()
    object GoToTransactionFailedScreen : SolwaveNavEvents()
    object GoToTransactionErrorScreen : SolwaveNavEvents()
    data class CloseFundsScreen(val context: Context) : SolwaveNavEvents()
}
