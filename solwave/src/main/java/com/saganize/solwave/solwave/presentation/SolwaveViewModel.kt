package com.saganize.solwave.solwave.presentation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.saganize.solwave.R
import com.saganize.solwave.core.di.TAG
import com.saganize.solwave.core.events.SolwaveAuthNavEvents
import com.saganize.solwave.core.events.SolwaveEvents
import com.saganize.solwave.core.events.SolwaveNavEvents
import com.saganize.solwave.core.models.CompleteEvents
import com.saganize.solwave.core.models.DeeplinkActionType
import com.saganize.solwave.core.models.NO_USER_FOUND
import com.saganize.solwave.core.models.Screens
import com.saganize.solwave.core.models.SolwaveErrors
import com.saganize.solwave.core.models.StartEvents
import com.saganize.solwave.core.models.WalletInfo
import com.saganize.solwave.core.models.WalletProvider
import com.saganize.solwave.core.models.ifSuccess
import com.saganize.solwave.core.models.jsonToError
import com.saganize.solwave.core.util.extensions.closeActivity
import com.saganize.solwave.core.util.extensions.defaultStringify
import com.saganize.solwave.core.util.extensions.getPublicKey
import com.saganize.solwave.core.util.extensions.getSharedSecret
import com.saganize.solwave.core.util.extensions.ifError
import com.saganize.solwave.core.util.extensions.ifSuccess
import com.saganize.solwave.core.util.extensions.showToast
import com.saganize.solwave.core.util.extensions.update
import com.saganize.solwave.core.util.toName
import com.saganize.solwave.data.remote.model.SimulationType
import com.saganize.solwave.data.remote.model.TransactionStatus
import com.saganize.solwave.domain.model.TransactionParams
import com.saganize.solwave.domain.model.TransactionPayload
import com.saganize.solwave.domain.model.TransactionResponse
import com.saganize.solwave.domain.model.WalletEntity
import com.saganize.solwave.domain.usecases.GenerateSignMessage
import com.saganize.solwave.domain.usecases.GetBalance
import com.saganize.solwave.domain.usecases.InitiateCreateUser
import com.saganize.solwave.domain.usecases.InitiateLogin
import com.saganize.solwave.domain.usecases.SimulateTransaction
import com.saganize.solwave.domain.usecases.UseCases
import com.saganize.solwave.solwave.model.SolwaveState
import com.saganize.solwave.solwave.presentation.components.generatePaymentDeepLink
import com.solana.Solana
import com.solana.api.getSignatureStatuses
import com.solana.core.Transaction
import com.solana.models.SignatureStatusRequestConfiguration
import com.solana.networking.HttpNetworkingRouter
import com.solana.networking.RPCEndpoint
import com.solana.programs.SystemProgram
import com.solana.vendor.TweetNaclFast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.bitcoinj.core.Base58
import java.nio.ByteBuffer
import java.nio.ByteOrder

class SolwaveViewModel(
    private val useCases: UseCases,
    private val start: String,
    private val message: String,
    private val transaction: Transaction,
    private val apiKey: String,
    private val completeEvents: CompleteEvents,
    private val isConnected: Boolean,
) : ViewModel() {
    private val _state =
        mutableStateOf(
            SolwaveState(
                transactionParams =
                    TransactionParams(
                        data =
                            TransactionPayload(
                                transaction = transaction,
                            ),
                    ),
            ),
        )
    val state: State<SolwaveState> = _state
    private var solana: Solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))

    init {
        initialize()
    }

    private fun initialize() {
        viewModelScope.launch {
            setupEncryptionKeyPair()
            handleStartEvents()
        }
    }

    private suspend fun setupEncryptionKeyPair() {
        useCases.getEncryptionKeyPair.execute(Unit).let { keyPair ->
            if (keyPair == null) {
                val newKeyPair = generateKeyPair()
                useCases.saveEncryptionKeyPair.execute(newKeyPair)
                _state.update { copy(keyPair = newKeyPair) }
            } else {
                _state.update { copy(keyPair = keyPair) }
            }
        }
    }

    private fun handleStartEvents() {
        if (!isConnected) {
            onEvent(
                SolwaveEvents.Error(
                    "No Internet Connection",
                    SolwaveErrors.NoInternetConnectionMessage,
                ),
            )
        } else if (start == StartEvents.SELECT.event) {
            viewModelScope.launch {
                val selected = useCases.getWalletsPreference.execute(Unit)
                val wallets = useCases.getWallets.execute(Unit)

                wallets?.find { it.name == selected }?.let { wallet ->
                    _state.update { copy(wallet = wallet.toWalletInfo()) }
                }

                _state.update { copy(screen = Screens.WalletScreen) }
            }
        } else if (start == StartEvents.SIGN_MESSAGE.event) {
            viewModelScope.launch {
                val selected =
                    useCases.getWalletsPreference.execute(Unit)
                        ?: throw IllegalStateException(SolwaveErrors.NoWalletSelectedMessage.message)

                val wallets = useCases.getWallets.execute(Unit)

                val message = this@SolwaveViewModel.message
                wallets?.find { it.name == selected }?.let { wallet ->
                    _state.update {
                        copy(
                            screen =
                                Screens.SignMessageScreen(
                                    message = message,
                                ),
                            message = message,
                            wallet = wallet.toWalletInfo(),
                        )
                    }
                }
            }
        } else {
            viewModelScope.launch {
                val selected =
                    useCases.getWalletsPreference.execute(Unit)
                        ?: throw IllegalStateException(SolwaveErrors.NoWalletSelectedMessage.message)

                val wallets = useCases.getWallets.execute(Unit)

                wallets?.find { it.name == selected }?.let { wallet ->
                    navigateToPayScreen(
                        transaction = transaction,
                        currentWallet = wallet.toWalletInfo(),
                    )
                }
            }
        }
    }

    fun onNav(event: SolwaveNavEvents) {
        when (event) {
            SolwaveNavEvents.GoToNoAccountScreen ->
                _state.update { copy(screen = Screens.NoAccountScreen) }

            SolwaveNavEvents.GoToLoginScreen ->
                _state.update { copy(screen = Screens.LoginScreen) }

            SolwaveNavEvents.GoToSignupScreen ->
                _state.update { copy(screen = Screens.SignupScreen) }

            SolwaveNavEvents.GoToPayScreen -> navigateToPayScreen(transaction = transaction)

            SolwaveNavEvents.GoToLoadingScreen ->
                _state.update { copy(screen = Screens.LoadingScreen) }

            SolwaveNavEvents.GoToNoFundsScreen ->
                _state.update { copy(screen = Screens.NoFundsScreen) }

            is SolwaveNavEvents.CloseWebViewScreen -> {
                _state.update { copy(url = null) }
                if (event.closeActivity) {
                    event.context.closeActivity()
                }
            }

            SolwaveNavEvents.GoToWalletScreen ->
                _state.update { copy(screen = Screens.WalletScreen) }

            is SolwaveNavEvents.CloseFundsScreen -> {
                _state.update { copy(error = SolwaveErrors.FundsErrorMessage) }

                event.context.closeActivity()
            }

            SolwaveNavEvents.GoToTransactionDoneScreen -> {}
            SolwaveNavEvents.GoToTransactionErrorScreen -> {}
            SolwaveNavEvents.GoToTransactionFailedScreen -> {}
        }
    }

    fun onAuthEvent(event: SolwaveAuthNavEvents) {
        when (event) {
            is SolwaveAuthNavEvents.InitCreateUser -> {
                event.user?.let { user ->
                    viewModelScope.launch {
                        user.getIdToken(true).await().token?.let { token ->
                            user.email?.let { email ->
                                val params =
                                    InitiateCreateUser.Params(
                                        email = email,
                                        verifyToken = token,
                                    )

                                useCases.initiateCreateUser.execute(params)
                                    .ifSuccess { response ->

                                        if (response?.errors?.isNotEmpty() == true) {
                                            Log.e(
                                                TAG,
                                                "Error: ${response.errors.firstOrNull()?.message}",
                                            )
                                            SolwaveEvents.Error(
                                                error =
                                                    SolwaveErrors.InitCreateUserErrorMessage.setError(
                                                        response.errors.firstOrNull()?.message
                                                            ?: SolwaveErrors.InitCreateUserErrorMessage.message,
                                                    ),
                                            )
                                        }

                                        response?.data?.firstOrNull()?.let {
                                            _state.update {
                                                copy(
                                                    url =
                                                        it.url + "?access-token=" +
                                                            it.accessToken + "&api-key=" + apiKey,
                                                )
                                            }
                                        }
                                    }.ifError {
                                        Log.e(TAG, "Error: $it")
                                        onEvent(
                                            SolwaveEvents.Error(
                                                "User Creation Failed",
                                                error =
                                                    SolwaveErrors.InitCreateUserErrorMessage.setError(
                                                        it.jsonToError(),
                                                    ),
                                            ),
                                        )
                                        firebaseSignOut(event.context)
                                    }
                            }
                        }
                    }
                } ?: {
                    Log.e(TAG, "No authenticated user")
                    SolwaveEvents.Error(
                        error =
                            SolwaveErrors.GenericErrorMsg.setError(
                                event.context.getString(
                                    R.string.session_expired_feedback,
                                ),
                            ),
                    )
                }
            }

            is SolwaveAuthNavEvents.InitiateLogin -> {
                event.user?.let { user ->
                    viewModelScope.launch {
                        user.getIdToken(true).await().token?.let { token ->
                            user.email?.let { email ->
                                val params =
                                    InitiateLogin.Params(
                                        email = email,
                                        verifyToken = token,
                                    )

                                useCases.initiateLogin.execute(params)
                                    .ifSuccess { response ->
                                        if (response?.errors?.isNotEmpty() == true) {
                                            Log.e(
                                                TAG,
                                                "Error: ${response.errors.firstOrNull()?.message}",
                                            )
                                            SolwaveEvents.Error(
                                                error =
                                                    SolwaveErrors.InitLoginErrorMessage.setError(
                                                        response.errors.firstOrNull()?.message
                                                            ?: SolwaveErrors.InitLoginErrorMessage.message,
                                                    ),
                                            )
                                        }
                                        response?.data?.firstOrNull()?.let {
                                            _state.update {
                                                copy(
                                                    url =
                                                        it.url + "?access-token=" + it.accessToken +
                                                            "&api-key=" + apiKey + "&email=" + email,
                                                )
                                            }
                                        }
                                    }.ifError {
                                        firebaseSignOut(event.context)
                                        onEvent(
                                            SolwaveEvents.Error(
                                                title = "Login Failed",
                                                SolwaveErrors.InitLoginErrorMessage.setError(
                                                    it.jsonToError(),
                                                ),
                                            ),
                                        )
                                        event.context.showToast(NO_USER_FOUND)
                                    }
                            }
                        }
                    }
                } ?: {
                    Log.e(TAG, "No authenticated user")
                    SolwaveEvents.Error(
                        error =
                            SolwaveErrors.GenericErrorMsg.setError(
                                event.context.getString(
                                    R.string.session_expired_feedback,
                                ),
                            ),
                    )
                }
            }

            is SolwaveAuthNavEvents.InitiateTransaction -> {
                viewModelScope.launch {
                    _state.value.wallet?.let { currentWallet ->

                        useCases.initiateTransaction.execute(currentWallet.key)
                            .ifSuccess { response ->
                                if (response?.errors?.isNotEmpty() == true) {
                                    Log.e(TAG, "Error: ${response.errors.firstOrNull()?.message}")
                                    SolwaveEvents.Error(
                                        error =
                                            SolwaveErrors.InitiateTransactionErrorMessage.setError(
                                                response.errors.firstOrNull()?.message
                                                    ?: SolwaveErrors.InitiateTransactionErrorMessage.message,
                                            ),
                                    )
                                }
                                response?.data?.firstOrNull()?.let {
                                    _state.update {
                                        copy(
                                            url =
                                                it.url + "?access-token=" + it.authToken +
                                                    "&api-key=" + apiKey,
                                        )
                                    }
                                }
                            }.ifError {
                                Log.e(TAG, "Error: $it")
                                onEvent(
                                    SolwaveEvents.TransactionFailed(
                                        SolwaveErrors.InitiateTransactionErrorMessage.setError(
                                            it.jsonToError(),
                                        ),
                                    ),
                                )
                                firebaseSignOut(event.context)
                            }
                    }
                }
            }

            is SolwaveAuthNavEvents.InitiateSignMessage -> {
                viewModelScope.launch {
                    _state.value.wallet?.let { wallet ->
                        useCases.initiateSignMessage.execute(wallet.key)
                            .ifSuccess { response ->
                                if (response?.errors?.isNotEmpty() == true) {
                                    Log.e(TAG, "Error: ${response.errors.firstOrNull()?.message}")
                                    SolwaveEvents.Error(
                                        error =
                                            SolwaveErrors.InitiateSignMessageErrorMessage.setError(
                                                response.errors.firstOrNull()?.message
                                                    ?: SolwaveErrors.InitiateSignMessageErrorMessage.message,
                                            ),
                                    )
                                }
                                response?.data?.firstOrNull()?.let {
                                    _state.update {
                                        copy(
                                            url =
                                                it.url + "?access-token=" + it.authToken +
                                                    "&api-key=" + apiKey,
                                        )
//                                        copy(
//                                            url = "http://192.168.29.224:5173/${it.idempotencyId}/transact" + "?access-token=" + it.authToken +
//                                                    "&api-key=" + apiKey,
//                                        )
                                    }
                                }
                            }.ifError { error ->
                                Log.e(TAG, "Error: $error")
                                onEvent(
                                    SolwaveEvents.Error(
                                        error =
                                            SolwaveErrors.InitiateSignMessageErrorMessage.setError(
                                                error.jsonToError(),
                                            ),
                                    ),
                                )
                                firebaseSignOut(event.context)
                            }
                    }
                }
            }

            is SolwaveAuthNavEvents.OnCreateUserDone -> {
                event.key?.let {
                    onEvent(
                        SolwaveEvents.SaveWallet(
                            event.context,
                            WalletProvider.Saganize,
                            it,
                        ),
                    )
                }
            }

            is SolwaveAuthNavEvents.OnLoginDone -> {
                event.key?.let {
                    onEvent(SolwaveEvents.SaveWallet(event.context, WalletProvider.Saganize, it))
                }
            }
        }
    }

    fun onEvent(event: SolwaveEvents) {
        when (event) {
            is SolwaveEvents.SelectWallet -> {
                getWallets(event.context, event.wallet, event.openDeepLink)
            }

            is SolwaveEvents.SaveWalletFromWebview -> {
                viewModelScope.launch {
                    useCases.saveWallet.execute(
                        WalletEntity(
                            name = event.wallet.toName(),
                            key = event.Key,
                        ),
                    ).let {
                        _state.update {
                            copy(
                                wallet = WalletInfo(event.wallet, event.Key),
                                screen = Screens.WalletScreen,
                            )
                        }
                    }
                    useCases.saveWalletPreference.execute(event.wallet.toName())
                }
            }

            is SolwaveEvents.SaveWallet -> {
                viewModelScope.launch {
                    useCases.saveWallet.execute(
                        WalletEntity(
                            name = event.wallet.toName(),
                            key = event.key,
                        ),
                    )
                    _state.update {
                        copy(
                            wallet = WalletInfo(event.wallet, event.key),
                            screen = Screens.WalletScreen,
                        )
                    }
                    navigateToPayScreen(transaction)
                    useCases.saveWalletPreference.execute(event.wallet.toName())
                }

                event.context.closeActivity()
            }

            is SolwaveEvents.PayUsingWallet -> {
                viewModelScope.launch {
                    useCases.getLatestBlockHash.execute(Unit)?.let { blockHash ->
                        _state.value.wallet?.let { wallet ->
                            _state.value.keyPair?.let { keypair ->
                                val deeplink =
                                    generatePaymentDeepLink(
                                        wallet,
                                        blockHash,
                                        keypair,
                                        transaction,
                                    )

                                _state.update { copy(deepLink = deeplink) }
                                event.openDeepLink()
                            }
                        }
                    }
                }
            }

            is SolwaveEvents.SignMessageUsingWallet -> {
                viewModelScope.launch {
                    _state.value.wallet?.let { wallet ->
                        _state.value.keyPair?.let { keypair ->

                            val params =
                                GenerateSignMessage.Params(
                                    event.message,
                                    wallet,
                                    keypair,
                                )

                            useCases.generateSignMessage.execute(params).ifSuccess { deeplink ->
                                _state.update { copy(deepLink = deeplink) }
                                event.openDeepLink()
                            }
                        }
                    }
                }
            }

            is SolwaveEvents.Error -> {
                _state.update {
                    copy(
                        error = event.error,
                        screen =
                            Screens.ErrorScreen(
                                title = event.title,
                            ),
                        url = if (event.closeWebView) null else _state.value.url,
                    )
                }
            }

            is SolwaveEvents.TransactionFailed -> {
                _state.update {
                    copy(error = event.error, screen = Screens.TransactionFailedScreen)
                }
            }

            is SolwaveEvents.DecryptTransactionResult -> {
                val data = event.data
                val nonce = event.nonce
                val walletProviderName = state.value.wallet?.walletProvider?.toName() ?: return
                val keypair = state.value.keyPair ?: return

                viewModelScope.launch {
                    try {
                        useCases.getWallets.execute(Unit)
                            ?.find { it.name == walletProviderName }
                            ?.key
                            ?.getSharedSecret()
                            ?.run {
                                val privateKeyBytes = Base58.decode(keypair.second)
                                val nonceBytes = Base58.decode(nonce)
                                val dataBytes = Base58.decode(data)
                                val phantomKeyBytes = Base58.decode(this)

                                val box = TweetNaclFast.Box(phantomKeyBytes, privateKeyBytes)
                                val decryptedDataBytes = box.open(dataBytes, nonceBytes)
                                val jsonData = String(decryptedDataBytes, Charsets.UTF_8)

                                Log.d(TAG, "Decrypted transaction data: $jsonData")

                                Gson().fromJson(jsonData, TransactionResponse::class.java)
                            }?.let {
                                _state.update { copy(screen = Screens.LoadingScreen) }
                                checkTransactionStatus(it.signature)
                            }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error: $e")
                        SolwaveEvents.Error(
                            error =
                                SolwaveErrors.GenericErrorMsg.setError(
                                    SolwaveErrors.GenericErrorMsg.message,
                                ),
                        )
                    }
                }
            }

            is SolwaveEvents.DecryptSignedMessageResult -> {
                val data = event.data
                val nonce = event.nonce
                val walletProviderName = state.value.wallet?.walletProvider?.toName() ?: return
                val keypair = state.value.keyPair ?: return

                viewModelScope.launch {
                    try {
                        useCases.getWallets.execute(Unit)
                            ?.find { it.name == walletProviderName }
                            ?.key
                            ?.getSharedSecret()
                            ?.run {
                                val privateKeyBytes = Base58.decode(keypair.second)
                                val nonceBytes = Base58.decode(nonce)
                                val dataBytes = Base58.decode(data)
                                val phantomKeyBytes = Base58.decode(this)

                                val box = TweetNaclFast.Box(phantomKeyBytes, privateKeyBytes)
                                val decryptedDataBytes = box.open(dataBytes, nonceBytes)
                                val jsonData = String(decryptedDataBytes, Charsets.UTF_8)

                                Log.d(TAG, "Decrypted signed message data: $jsonData")

                                Gson().fromJson(jsonData, TransactionResponse::class.java)
                            }?.let {
                                _state.update { copy(messageSignature = it.signature) }
                                event.context.closeActivity()
                            }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error: $e")
                        SolwaveEvents.Error(
                            error =
                                SolwaveErrors.GenericErrorMsg.setError(
                                    SolwaveErrors.GenericErrorMsg.message,
                                ),
                        )
                    }
                }
            }

            is SolwaveEvents.TransactionDone -> {
                _state.update { copy(screen = Screens.LoadingScreen) }
                checkTransactionStatus(event.id)
            }

            is SolwaveEvents.MessageSigned -> {
                _state.update { copy(messageSignature = event.signature) }
            }
        }
    }

    private fun getWallets(
        context: Context,
        wallet: WalletProvider,
        openDeepLink: () -> Unit,
    ) {
        viewModelScope.launch {
            useCases.getWallets.execute(Unit)?.find { it.name == wallet.toName() }
                .let {
                    if (it != null) {
                        _state.update { copy(wallet = it.toWalletInfo()) }
                        useCases.saveWalletPreference.execute(wallet.toName())
                        context.closeActivity()
                    } else {
                        when (wallet) {
                            WalletProvider.Saganize -> {
                                _state.update {
                                    copy(
                                        screen = Screens.NoAccountScreen,
                                    )
                                }
                            }

                            else -> {
                                openDeepLink.invoke()
                            }
                        }
                    }
                }
        }
    }

    private fun navigateToPayScreen(
        transaction: Transaction,
        currentWallet: WalletInfo? = null,
    ) {
        val wallet = currentWallet ?: state.value.wallet ?: return

        viewModelScope.launch {
            val params =
                SimulateTransaction.Params(
                    transaction = transaction.defaultStringify(),
                    publicKey = wallet.key.getPublicKey(),
                )
            useCases.simulateTransaction.execute(params)
                .ifSuccess { response ->
                    if (response?.errors?.isNotEmpty() == true) {
                        Log.e(TAG, "Error: ${response.errors.firstOrNull()?.message}")
                        SolwaveEvents.Error(
                            error =
                                SolwaveErrors.GenericErrorMsg.setError(
                                    response.errors.firstOrNull()?.message
                                        ?: SolwaveErrors.GenericErrorMsg.message,
                                ),
                        )
                    }

                    Log.d(TAG, "Response: $response")

                    response?.data?.firstOrNull()?.let {
                        val tsxStatus =
                            when (it.status) {
                                "success" -> TransactionStatus.SUCCESS
                                else -> TransactionStatus.FAILED
                            }

                        val simulationType = getTransactionType()

                        val transactionPayload =
                            _state.value.transactionParams.data.copy(
                                transaction = transaction,
                                fees = it.networkFee,
                            )

                        _state.update {
                            copy(
                                transactionParams =
                                    _state.value.transactionParams.copy(
                                        type = simulationType,
                                        status = tsxStatus,
                                        data = transactionPayload,
                                    ),
                                screen =
                                    Screens.PayScreen(
                                        transactionParams =
                                            TransactionParams(
                                                type = simulationType,
                                                status = tsxStatus,
                                                data = transactionPayload,
                                            ),
                                    ),
                                url = null,
                                wallet = wallet,
                            )
                        }
                    }
                }.ifError {
                    Log.e(TAG, "Error: $it")
                    SolwaveEvents.Error(
                        error =
                            SolwaveErrors.GenericErrorMsg.setError(
                                it.jsonToError(),
                            ),
                    )
                }
        }
    }

    private fun getTransactionType(): SimulationType {
        transaction.instructions.forEach { instruction ->

            if (instruction.programId == SystemProgram.PROGRAM_ID) {
                val fromAddress = instruction.keys[0].publicKey

                val toAddress = instruction.keys[1].publicKey

                val lamports =
                    ByteBuffer
                        .wrap(instruction.data, 4, 8)
                        .order(ByteOrder.LITTLE_ENDIAN)
                        .long

                val transactionParams =
                    _state.value.transactionParams.copy(
                        data =
                            _state.value.transactionParams.data.copy(
                                from = fromAddress.toString(),
                                to = toAddress.toString(),
                                lamports = lamports.toDouble(),
                            ),
                    )
                _state.update {
                    copy(
                        transactionParams = transactionParams,
                    )
                }
                return SimulationType.TRANSFER
            }
        }
        return SimulationType.OTHER
    }

    private fun checkTransactionStatus(transactionId: String) =
        viewModelScope.launch {
            val list = listOf(transactionId)
            val config = SignatureStatusRequestConfiguration(false)

            repeat(10) {
                val status =
                    withContext(Dispatchers.IO) {
                        try {
                            val signature =
                                solana.api.getSignatureStatuses(list, config).getOrNull()
                                    ?: return@withContext {
                                        onEvent(
                                            SolwaveEvents.TransactionFailed(
                                                SolwaveErrors.InvalidTransactionMessage,
                                            ),
                                        )
                                    }

                            signature[0].confirmationStatus.toString().also {
                                Log.d(TAG, "Transaction status: $signature")
                            }
                        } catch (e: Exception) {
                            Log.d(TAG, "Transaction status: error", e)
                            null
                        }
                    }

                if (status == "finalized") {
                    _state.update {
                        copy(
                            screen = Screens.TransactionDoneScreen,
                            transactionId = transactionId,
                        )
                    }
                    return@launch // Exit after successful update
                }

                delay(2000)
            }

            _state.update {
                copy(
                    error = SolwaveErrors.VerificationErrorMessage,
                    screen = Screens.TransactionFailedScreen,
                )
            }
        }

    fun getBalance() {
        val publicKey = state.value.wallet?.key?.getPublicKey() ?: return

        viewModelScope.launch {
            val params =
                GetBalance.Params(
                    walletAddress = publicKey,
                )
            val balance = useCases.getBalance.execute(params)
            _state.update { copy(balance = balance) }
        }
    }

    private fun firebaseSignOut(context: Context) {
        val googleSignInClient =
            GoogleSignIn.getClient(
                context,
                GoogleSignInOptions.DEFAULT_SIGN_IN,
            )
        val firebaseAuth = FirebaseAuth.getInstance()

        googleSignInClient.signOut().addOnCompleteListener {
            firebaseAuth.signOut()
        }
    }

    private fun generateKeyPair(): Pair<String, String> {
        val keyPair = TweetNaclFast.Box.keyPair()
        val publicKey: String = Base58.encode(keyPair.publicKey)
        val secretKey: String = Base58.encode(keyPair.secretKey)
        return Pair(publicKey, secretKey)
    }

    private fun setLoading(loading: Boolean) {
        _state.update { copy(loading = loading) }
    }

    fun updateDeeplinkActionType(deeplinkActionType: DeeplinkActionType?) {
        _state.update { copy(deeplinkActionType = deeplinkActionType) }
    }

    override fun onCleared() {
        val result =
            when (start) {
                StartEvents.SELECT.event ->
                    state.value.wallet?.key?.let {
                        if (state.value.wallet?.walletProvider == WalletProvider.Saganize) {
                            it
                        } else {
                            it.getPublicKey()
                        }
                    }

                StartEvents.SIGN_MESSAGE.event -> state.value.messageSignature

                else -> state.value.transactionId
            }

        state.value.error?.let { completeEvents.onFailure(it) } ?: result?.let {
            completeEvents.onSuccess(
                it,
            )
        }

        _state.update { copy(url = null, deeplinkActionType = null) }

        super.onCleared()
    }
}
