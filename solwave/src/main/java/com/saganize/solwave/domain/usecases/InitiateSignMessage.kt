package com.saganize.solwave.domain.usecases

import com.saganize.solwave.data.remote.model.NetworkResponse
import com.saganize.solwave.data.remote.model.requests.InitiateSignMessageRequest
import com.saganize.solwave.data.remote.model.response.InitiateSignMessageResponse
import com.saganize.solwave.domain.repository.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class InitiateSignMessage(
    private val apiRepository: ApiRepository,
) : UseCase<String, Response<NetworkResponse<List<InitiateSignMessageResponse>>>> {

    override suspend fun execute(input: String): Response<NetworkResponse<List<InitiateSignMessageResponse>>> {
        return withContext(Dispatchers.IO) {
            apiRepository.initiateSignMessage(
                InitiateSignMessageRequest(
                    publicKey = input,
                ),
            )
        }
    }
}
