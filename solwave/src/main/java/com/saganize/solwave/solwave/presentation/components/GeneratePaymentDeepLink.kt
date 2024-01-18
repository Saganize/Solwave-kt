package com.saganize.solwave.solwave.presentation.components

import android.util.Log
import com.google.gson.Gson
import com.iwebpp.crypto.TweetNacl
import com.saganize.solwave.core.di.TAG
import com.saganize.solwave.core.models.WalletInfo
import com.saganize.solwave.core.models.WalletProvider
import com.saganize.solwave.core.util.extensions.splitWalletKey
import com.saganize.solwave.domain.model.SolWalletKey
import com.solana.core.PublicKey
import com.solana.core.SerializeConfig
import com.solana.core.Transaction
import com.solana.vendor.TweetNaclFast
import org.bitcoinj.core.Base58
import org.json.JSONObject

fun generatePaymentDeepLink(
    wallet: WalletInfo,
    blockHash: String,
    encryptionKeyPair: Pair<String, String>,
    transaction: Transaction,
): String {
    val (publicKey, session, sharedSecret) = wallet.key.splitWalletKey()

    val tsx = transaction.apply {
        setRecentBlockHash(blockHash)
        feePayer = PublicKey(publicKey)
    }

    val serializedTransaction = Base58.encode(
        tsx.serialize(
            SerializeConfig(
                requireAllSignatures = false,
                verifySignatures = false,
            ),
        ),
    )

    val payloadMap = mapOf(
        "transaction" to serializedTransaction,
        "session" to session,
    )

    val nonceBytes = TweetNaclFast.randombytes(24)
    val payloadJsonBytes = JSONObject(payloadMap).toString().toByteArray()
    val sharedSecretBytes = Base58.decode(sharedSecret)

    val box = TweetNacl.Box(sharedSecretBytes, Base58.decode(encryptionKeyPair.second))
        .after(payloadJsonBytes, nonceBytes)

    val encryptedPayload = Base58.encode(box)

    val dpInit =
        if (wallet.walletProvider == WalletProvider.Phantom) {
            "https://phantom.app/ul/v1/signAndSendTransaction?"
        } else {
            "https://solflare.com/ul/v1/signAndSendTransaction?"
        }

    return dpInit +
        "&dapp_encryption_public_key=${encryptionKeyPair.first}" +
        "&nonce=${Base58.encode(nonceBytes)}" +
        "&redirect_link=app%3A%2F%2Fsolwave.com%2Fdeeplink" +
        "&payload=$encryptedPayload"
}

fun decryptData(walletKey: String, privateKey: String, nonce: String, data: String): String? {
    return try {
        val privateKeyBytes = Base58.decode(privateKey)
        val phantomKeyBytes = Base58.decode(walletKey)
        val nonceBytes = Base58.decode(nonce)
        val dataBytes = Base58.decode(data)

        val box = TweetNaclFast.Box(phantomKeyBytes, privateKeyBytes)
        val decryptedDataBytes = box.open(dataBytes, nonceBytes)
        val jsonData = String(decryptedDataBytes, Charsets.UTF_8)

        val solWalletKey = Gson().fromJson(jsonData, SolWalletKey::class.java)

        solWalletKey.public_key + "/" + solWalletKey.session + "/" + walletKey
    } catch (e: Exception) {
        Log.e(TAG, "decryptData: ${e.message}")
        null
    }
}
