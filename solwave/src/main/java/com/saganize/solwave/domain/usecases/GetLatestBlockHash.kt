package com.saganize.solwave.domain.usecases

import android.util.Log
import com.saganize.solwave.core.di.TAG
import com.saganize.solwave.solwave.presentation.components.getLatestBlockhash
import com.solana.Solana
import com.solana.networking.HttpNetworkingRouter
import com.solana.networking.RPCEndpoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetLatestBlockHash : UseCase<Unit, String?> {
    private var solana: Solana = Solana(HttpNetworkingRouter(RPCEndpoint.devnetSolana))

    override suspend fun execute(input: Unit): String? {
        return withContext(Dispatchers.IO) {
            try {
                val result = solana.api.getLatestBlockhash()
                result.getOrNull()
            } catch (e: Exception) {
                Log.e(TAG, "Error getting latest blockhash", e)
                null
            }
        }
    }
}
