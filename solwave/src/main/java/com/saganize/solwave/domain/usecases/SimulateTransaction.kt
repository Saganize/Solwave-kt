package com.saganize.solwave.domain.usecases

import com.saganize.solwave.data.remote.model.NetworkResponse
import com.saganize.solwave.data.remote.model.requests.SimulateTransactionRequest
import com.saganize.solwave.data.remote.model.response.SimulateTransactionResponse
import com.saganize.solwave.domain.repository.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class SimulateTransaction(
    private val apiRepository: ApiRepository,
) : UseCase<SimulateTransaction.Params, Response<NetworkResponse<List<SimulateTransactionResponse>>>> {

    data class Params(
        val transaction: String,
        val publicKey: String,
    )

    override suspend fun execute(input: Params): Response<NetworkResponse<List<SimulateTransactionResponse>>> {
        return withContext(Dispatchers.IO) {
            apiRepository.simulateTransaction(
                SimulateTransactionRequest(
                    transaction = input.transaction,
                    publicKey = input.publicKey,
                ),
            )
        }
    }
}
