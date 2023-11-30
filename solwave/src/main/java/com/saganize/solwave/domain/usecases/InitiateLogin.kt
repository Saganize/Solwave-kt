package com.saganize.solwave.domain.usecases

import com.saganize.solwave.data.remote.model.NetworkResponse
import com.saganize.solwave.data.remote.model.requests.InitiateAuthRequest
import com.saganize.solwave.data.remote.model.response.InitiateAuthResponse
import com.saganize.solwave.domain.repository.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class InitiateLogin(
    private val apiRepository: ApiRepository,
) : UseCase<InitiateLogin.Params, Response<NetworkResponse<List<InitiateAuthResponse>>>> {

    data class Params(
        val email: String,
        val verifyToken: String,
    )

    override suspend fun execute(input: Params): Response<NetworkResponse<List<InitiateAuthResponse>>> {
        return withContext(Dispatchers.IO) {
            apiRepository.initiateLogin(
                InitiateAuthRequest(
                    email = input.email,
                    verifyToken = input.verifyToken,
                ),
            )
        }
    }
}
