package com.saganize.solwave

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.Keep
import com.google.firebase.FirebaseApp
import com.saganize.solwave.core.di.TAG
import com.saganize.solwave.core.models.CompleteEvents
import com.saganize.solwave.core.models.EventKeys
import com.saganize.solwave.core.models.FIREBASE_NOT_INITIALIZED
import com.saganize.solwave.core.models.SolwaveErrors
import com.saganize.solwave.core.models.StartEvents
import com.saganize.solwave.core.util.extensions.stringifyCustomSerializer
import com.saganize.solwave.core.util.extensions.validate
import com.solana.core.Transaction

lateinit var completeEvents: CompleteEvents

@Keep
class Solwave(
    private val context: Context,
    private val apiKey: String,
) {

    init {
        initializeFirebase()
    }

    fun selectWallet(
        onSuccess: (result: String) -> Unit,
        onFailure: (error: SolwaveErrors) -> Unit,
    ) {
        val intent = createIntent(
            StartEvents.SELECT,
            onSuccess = onSuccess,
            onFailure = onFailure,
        )

        context.startActivity(intent)
    }

    fun signMessage(
        message: String,
        onSuccess: (result: String) -> Unit,
        onFailure: (error: SolwaveErrors) -> Unit,
    ) {
        if (validateMessage(message).not()) {
            onFailure(SolwaveErrors.InvalidSigningMessage)
            return
        }
        val intent = createIntent(
            StartEvents.SIGN_MESSAGE,
            message = message,
            onSuccess = onSuccess,
            onFailure = onFailure,
        )

        context.startActivity(intent)
    }

    fun performTransaction(
        transaction: Transaction,
        onSuccess: (result: String) -> Unit,
        onFailure: (error: SolwaveErrors) -> Unit,
    ) {
        if (transaction.validate().not()) {
            onFailure(SolwaveErrors.InvalidTransactionMessage)
            return
        }

        val serializedTransaction = transaction.stringifyCustomSerializer()

        val intent = createIntent(
            startEvent = StartEvents.PAY,
            serializedTransaction = serializedTransaction,
            onSuccess = onSuccess,
            onFailure = onFailure,
        )

        context.startActivity(intent)
    }

    private fun createIntent(
        startEvent: StartEvents,
        message: String? = null,
        serializedTransaction: String? = null,
        onSuccess: (result: String) -> Unit,
        onFailure: (error: SolwaveErrors) -> Unit,
    ): Intent {
        completeEvents = CompleteEvents(onSuccess, onFailure)

        val intent = Intent(context, SolwaveActivity::class.java).apply {
            putExtra(EventKeys.START.key, startEvent.event)
            putExtra(EventKeys.API_KEY.key, apiKey)
            // putExtra(EventKeys.EVENT.key, CompleteEvents(onSuccess, onFailure))
            message?.let { putExtra(EventKeys.MESSAGE.key, it) }
            serializedTransaction?.let { putExtra(EventKeys.TRANSACTION.key, it) }
        }
        return intent
    }

    private fun initializeFirebase() {
        try {
            FirebaseApp.getInstance()
        } catch (e: IllegalStateException) {
            throw IllegalStateException(FIREBASE_NOT_INITIALIZED)
        }
    }

    private fun validateMessage(message: String): Boolean {
        if ((message.length <= SIGNING_MESSAGE_MAX_LENGTH).not()) {
            Log.e(TAG, "Message length is greater than $SIGNING_MESSAGE_MAX_LENGTH")
            return false
        }

        return true
    }

    companion object {
        const val SIGNING_MESSAGE_MAX_LENGTH = 300
    }
}