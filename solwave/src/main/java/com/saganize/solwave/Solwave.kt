package com.saganize.solwave

import android.content.Context
import android.content.Intent
import androidx.annotation.Keep
import com.google.firebase.FirebaseApp
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
        serializedTransaction: String? = null,
        onSuccess: (result: String) -> Unit,
        onFailure: (error: SolwaveErrors) -> Unit,
    ): Intent {
        completeEvents = CompleteEvents(onSuccess, onFailure)

        val intent = Intent(context, SolwaveActivity::class.java).apply {
            putExtra(EventKeys.START.key, startEvent.event)
            putExtra(EventKeys.API_KEY.key, apiKey)
            // putExtra(EventKeys.EVENT.key, CompleteEvents(onSuccess, onFailure))
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
}
