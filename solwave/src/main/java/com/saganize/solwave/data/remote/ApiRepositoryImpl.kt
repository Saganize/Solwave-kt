package com.saganize.solwave.data.remote

import com.saganize.solwave.data.remote.model.NetworkResponse
import com.saganize.solwave.data.remote.model.requests.InitiateAuthRequest
import com.saganize.solwave.data.remote.model.requests.InitiateSignMessageRequest
import com.saganize.solwave.data.remote.model.requests.InitiateTransactionRequest
import com.saganize.solwave.data.remote.model.requests.SimulateTransactionRequest
import com.saganize.solwave.data.remote.model.response.InitiateAuthResponse
import com.saganize.solwave.data.remote.model.response.InitiateSignMessageResponse
import com.saganize.solwave.data.remote.model.response.InitiateTransactionResponse
import com.saganize.solwave.data.remote.model.response.SimulateTransactionResponse
import com.saganize.solwave.domain.repository.ApiRepository
import retrofit2.Response

class ApiRepositoryImpl(
    private val api: SolwaveAPI,
    private val apiKey: String,
) : ApiRepository {

    override suspend fun initiateCreateUser(requestBody: InitiateAuthRequest):
        Response<NetworkResponse<List<InitiateAuthResponse>>> {
        return api.initiateCreateUser(
            requestBody = requestBody,
            api = apiKey,
        )
    }

    override suspend fun initiateLogin(requestBody: InitiateAuthRequest):
        Response<NetworkResponse<List<InitiateAuthResponse>>> {
        return api.initiateLogin(
            requestBody = requestBody,
            api = apiKey,
        )
    }

    override suspend fun initiateTransaction(requestBody: InitiateTransactionRequest):
        Response<NetworkResponse<List<InitiateTransactionResponse>>> {
        return api.initiateTransaction(
            requestBody = requestBody,
            api = apiKey,
        )
    }

    override suspend fun simulateTransaction(requestBody: SimulateTransactionRequest):
        Response<NetworkResponse<List<SimulateTransactionResponse>>> {
        return api.simulateTransaction(
            requestBody = requestBody,
            api = apiKey,
        )
    }

    override suspend fun initiateSignMessage(requestBody: InitiateSignMessageRequest):
        Response<NetworkResponse<List<InitiateSignMessageResponse>>> {
        return api.initiateSignMessage(
            requestBody = requestBody,
            api = apiKey,
        )
    }
}
