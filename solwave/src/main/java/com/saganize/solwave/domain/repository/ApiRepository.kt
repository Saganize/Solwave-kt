package com.saganize.solwave.domain.repository

import com.saganize.solwave.data.remote.model.NetworkResponse
import com.saganize.solwave.data.remote.model.requests.InitiateAuthRequest
import com.saganize.solwave.data.remote.model.requests.InitiateSignMessageRequest
import com.saganize.solwave.data.remote.model.requests.InitiateTransactionRequest
import com.saganize.solwave.data.remote.model.requests.SimulateTransactionRequest
import com.saganize.solwave.data.remote.model.response.InitiateAuthResponse
import com.saganize.solwave.data.remote.model.response.InitiateSignMessageResponse
import com.saganize.solwave.data.remote.model.response.InitiateTransactionResponse
import com.saganize.solwave.data.remote.model.response.SimulateTransactionResponse
import retrofit2.Response
import retrofit2.http.Body

interface ApiRepository {
    suspend fun initiateCreateUser(@Body requestBody: InitiateAuthRequest):
        Response<NetworkResponse<List<InitiateAuthResponse>>>

    suspend fun initiateLogin(@Body requestBody: InitiateAuthRequest):
        Response<NetworkResponse<List<InitiateAuthResponse>>>

    suspend fun initiateTransaction(@Body requestBody: InitiateTransactionRequest):
        Response<NetworkResponse<List<InitiateTransactionResponse>>>

    suspend fun simulateTransaction(@Body requestBody: SimulateTransactionRequest):
        Response<NetworkResponse<List<SimulateTransactionResponse>>>

    suspend fun initiateSignMessage(@Body requestBody: InitiateSignMessageRequest):
        Response<NetworkResponse<List<InitiateSignMessageResponse>>>
}
