package com.saganize.solwave.domain.usecases

import com.saganize.solwave.data.remote.model.NetworkResponse
import com.saganize.solwave.data.remote.model.requests.InitiateTransactionRequest
import com.saganize.solwave.data.remote.model.response.InitiateTransactionResponse
import com.saganize.solwave.domain.repository.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class InitiateTransaction(
    private val apiRepository: ApiRepository,
) : UseCase<String, Response<NetworkResponse<List<InitiateTransactionResponse>>>> {

    override suspend fun execute(input: String): Response<NetworkResponse<List<InitiateTransactionResponse>>> {
        return withContext(Dispatchers.IO) {
            apiRepository.initiateTransaction(
                InitiateTransactionRequest(
                    publicKey = input,
                ),
            )
        }
    }
}
