package com.saganize.solwave.core.events

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import com.solana.core.Transaction

sealed class SolwaveAuthNavEvents {
    data class InitCreateUser(
        val context: Context,
        val user: FirebaseUser?,
    ) : SolwaveAuthNavEvents()

    data class OnCreateUserDone(
        val context: Context,
        val key: String?,
    ) : SolwaveAuthNavEvents()

    data class InitiateLogin(
        val context: Context,
        val user: FirebaseUser?,
    ) : SolwaveAuthNavEvents()
    data class OnLoginDone(
        val context: Context,
        val key: String?,
    ) : SolwaveAuthNavEvents()

    data class InitiateTransaction(
        val context: Context,
        val user: Transaction?,
    ) : SolwaveAuthNavEvents()

    data class InitiateSignMessage(
        val context: Context,
        val publicKey: String,
    ) : SolwaveAuthNavEvents()
}
