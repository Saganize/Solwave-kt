package com.saganize.solwave

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.saganize.solwave.core.di.SolwaveAppModuleImpl
import com.saganize.solwave.core.events.SolwaveEvents
import com.saganize.solwave.core.models.EventKeys
import com.saganize.solwave.core.models.SolwaveErrors
import com.saganize.solwave.core.models.StartEvents
import com.saganize.solwave.core.models.WalletProvider
import com.saganize.solwave.core.util.ConnectionState
import com.saganize.solwave.core.util.connectivityState
import com.saganize.solwave.core.util.extensions.toTransaction
import com.saganize.solwave.core.util.viewModelFactory
import com.saganize.solwave.solwave.presentation.SolwaveScreen
import com.saganize.solwave.solwave.presentation.SolwaveViewModel
import com.saganize.solwave.solwave.presentation.components.decryptData
import kotlinx.coroutines.ExperimentalCoroutinesApi

class SolwaveActivity : ComponentActivity() {

    lateinit var viewModel: SolwaveViewModel

    @SuppressLint("SourceLockedOrientationActivity")
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        val start = intent.getStringExtra(EventKeys.START.key)
            ?: throw IllegalStateException(SolwaveErrors.NoStartEventMessage.message)

        val apiKey = intent.getStringExtra(EventKeys.API_KEY.key)
            ?: throw IllegalStateException(SolwaveErrors.NoApiKeyPassedMessage.message)

        val transactionString = if (start == StartEvents.PAY.event) {
            intent.getStringExtra(EventKeys.TRANSACTION.key)
                ?: throw IllegalStateException(SolwaveErrors.NoTransactionPassedMessage.message)
        } else {
            ""
        }

        // val completeEvents = intent?.parcelable<CompleteEvents>(EventKeys.EVENT.key)
        //    ?: CompleteEvents()

        setContent {
            val connection by connectivityState()
            val isConnected = connection === ConnectionState.Available

            val module = SolwaveAppModuleImpl(this, apiKey)
            viewModel = viewModel(
                factory = viewModelFactory {
                    SolwaveViewModel(
                        module.usecases,
                        start,
                        transactionString.toTransaction(),
                        apiKey,
                        completeEvents,
                        isConnected,
                    )
                },
            )

            if (!isConnected) {
                viewModel.onEvent(
                    SolwaveEvents.Error(
                        title = "No Internet Connection",
                        SolwaveErrors.NoInternetConnectionMessage,
                    ),
                )
            }

            SolwaveScreen(viewModel = viewModel)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val state = viewModel.state.value

        if (intent?.action == Intent.ACTION_VIEW) {
            val uri = intent.data
            val nonce = uri?.getQueryParameter("nonce") ?: ""
            val data = uri?.getQueryParameter("data") ?: ""

            uri?.getQueryParameter(getString(R.string.solflare_encryption_public_key))?.let {
                state.keyPair?.let { keypair ->
                    viewModel.onEvent(
                        SolwaveEvents.SaveWallet(
                            this,
                            WalletProvider.Solflare,
                            decryptData(it, keypair.second, nonce, data),
                        ),
                    )
                }
            }

            uri?.getQueryParameter(getString(R.string.phantom_encryption_public_key))?.let {
                state.keyPair?.let { keypair ->
                    viewModel.onEvent(
                        SolwaveEvents.SaveWallet(
                            this,
                            WalletProvider.Phantom,
                            decryptData(it, keypair.second, nonce, data),
                        ),
                    )
                }
            }

            if (
                uri?.getQueryParameter(getString(R.string.solflare_encryption_public_key))
                    .isNullOrEmpty() &&
                uri?.getQueryParameter(getString(R.string.phantom_encryption_public_key))
                    .isNullOrEmpty()
            ) {
                uri?.getQueryParameter("data")?.let {
                    viewModel.onEvent(SolwaveEvents.DecryptTransactionResult(it, nonce))
                }
            }

            uri?.getQueryParameter("errorMessage")?.let {
                viewModel.onEvent(
                    SolwaveEvents.Error(
                        error = SolwaveErrors.DeepLinkErrorMessage.setError(
                            it.split(":").getOrNull(1) ?: it,
                        ),
                    ),
                )
            }
        }
    }
}