package com.saganize.solwave.core.models

import com.saganize.solwave.domain.model.TransactionParams

sealed class Screens {
    object LoadingScreen : Screens()
    object LoginScreen : Screens()
    object NoAccountScreen : Screens()
    object SignupScreen : Screens()
    object WalletScreen : Screens()
    data class PayScreen(val transactionParams: TransactionParams) : Screens()
    data class SignMessageScreen(val message: String) : Screens()
    object NoFundsScreen : Screens()
    object TransactionDoneScreen : Screens()
    object TransactionFailedScreen : Screens()
    data class ErrorScreen(
        val title: String? = null,
    ) : Screens()
}
