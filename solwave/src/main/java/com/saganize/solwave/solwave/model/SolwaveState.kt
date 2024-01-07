package com.saganize.solwave.solwave.model

import com.saganize.solwave.core.models.DeeplinkActionType
import com.saganize.solwave.core.models.Screens
import com.saganize.solwave.core.models.SolwaveErrors
import com.saganize.solwave.core.models.WalletInfo
import com.saganize.solwave.domain.model.TransactionParams

data class SolwaveState(
    val screen: Screens? = null,
    val wallet: WalletInfo? = null,
    val message: String? = null,
    val messageSignature: String? = null,
    val transactionParams: TransactionParams,
    val url: String? = null,
    val deepLink: String? = null,
    val keyPair: Pair<String, String>? = null,
    val error: SolwaveErrors? = null,
    val transactionId: String = "",
    val balance: Double? = null,
    val loading: Boolean = false,
    val deeplinkActionType: DeeplinkActionType? = null,
) {
    companion object {
        const val MAX_SIGN_MESSAGE_LENGTH = 200
    }
}
