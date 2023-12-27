package com.saganize.solwave.domain.usecases

import com.iwebpp.crypto.TweetNacl
import com.saganize.solwave.core.models.DisplayFormat
import com.saganize.solwave.core.models.Response
import com.saganize.solwave.core.models.WalletInfo
import com.saganize.solwave.core.models.WalletProvider
import com.saganize.solwave.core.util.extensions.splitWalletKey
import com.solana.vendor.TweetNaclFast
import org.bitcoinj.core.Base58
import org.json.JSONObject

class GenerateSignMessage : UseCase<GenerateSignMessage.Params, Response<String>> {

    data class Params(
        val message: String,
        val wallet: WalletInfo,
        val encryptionKeyPair: Pair<String, String>,
        val displayFormat: DisplayFormat = DisplayFormat.UTF8,
    )

    override suspend fun execute(input: Params): Response<String> {
        return try {
            val (publicKey, session, sharedSecret) = input.wallet.key.splitWalletKey()

            val payloadMap = mapOf(
                "message" to Base58.encode(input.message.toByteArray()),
                "session" to session,
                "display" to input.displayFormat.format,
            )

            val nonceBytes = TweetNaclFast.randombytes(24)
            val payloadJsonBytes = JSONObject(payloadMap).toString().toByteArray()
            val sharedSecretBytes = Base58.decode(sharedSecret)

            val box =
                TweetNacl.Box(sharedSecretBytes, Base58.decode(input.encryptionKeyPair.second))
                    .after(payloadJsonBytes, nonceBytes)

            val encryptedPayload = Base58.encode(box)

            val dpInit = if (input.wallet.walletProvider == WalletProvider.Phantom) {
                "https://phantom.app/ul/v1/signMessage?"
            } else {
                "https://solflare.com/ul/v1/signMessage?"
            }

            return Response.Success(
                dpInit +
                    "&dapp_encryption_public_key=${input.encryptionKeyPair.first}" +
                    "&nonce=${Base58.encode(nonceBytes)}" +
                    "&redirect_link=app%3A%2F%2Fsolwave.com%2Fdeeplink" +
                    "&payload=$encryptedPayload",
            )
        } catch (e: Exception) {
            Response.Error(e)
        }
    }
}
