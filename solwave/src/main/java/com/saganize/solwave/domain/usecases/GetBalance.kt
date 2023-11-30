package com.saganize.solwave.domain.usecases

import android.util.Log
import com.saganize.solwave.core.di.TAG
import com.saganize.solwave.data.remote.getBalance
import com.solana.Solana
import com.solana.core.PublicKey
import com.solana.networking.Commitment
import com.solana.networking.HttpNetworkingRouter
import com.solana.networking.RPCEndpoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetBalance : UseCase<GetBalance.Params, Double?> {
    private var solana: Solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))

    data class Params(
        val walletAddress: String,
    )

    override suspend fun execute(input: Params): Double? {
        return withContext(Dispatchers.IO) {
            try {
                val result = solana.api.getBalance(
                    PublicKey(input.walletAddress),
                    commitment = Commitment.CONFIRMED,
                )
                result.getOrNull()?.toDouble()
            } catch (e: Exception) {
                Log.e(TAG, "Error getting balance", e)
                null // Return null if there's an exception
            }
        }
    }
}
