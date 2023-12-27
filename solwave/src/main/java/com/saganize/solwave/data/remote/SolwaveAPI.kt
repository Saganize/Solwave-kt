package com.saganize.solwave.data.remote

import androidx.annotation.Keep
import com.saganize.solwave.core.util.BackendEndpoints
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
import retrofit2.http.Header
import retrofit2.http.POST

@Keep
interface SolwaveAPI {

    @POST(BackendEndpoints.Auth.INITIATE_CREATION)
    suspend fun initiateCreateUser(
        @Header("api") api: String,
        @Body requestBody: InitiateAuthRequest,
    ): Response<NetworkResponse<List<InitiateAuthResponse>>>

    @POST(BackendEndpoints.Auth.INITIATE_LOGIN)
    suspend fun initiateLogin(
        @Header("api") api: String,
        @Body requestBody: InitiateAuthRequest,
    ): Response<NetworkResponse<List<InitiateAuthResponse>>>

    @POST(BackendEndpoints.Transaction.INITIATE_TRANSACTION)
    suspend fun initiateTransaction(
        @Header("api") api: String,
        @Body requestBody: InitiateTransactionRequest,
    ): Response<NetworkResponse<List<InitiateTransactionResponse>>>

    @POST(BackendEndpoints.Transaction.SIMULATE_TRANSACTION)
    suspend fun simulateTransaction(
        @Header("api") api: String,
        @Body requestBody: SimulateTransactionRequest,
    ): Response<NetworkResponse<List<SimulateTransactionResponse>>>

    @POST(BackendEndpoints.Transaction.SIGN_MESSAGE)
    suspend fun initiateSignMessage(
        @Header("api") api: String,
        @Body requestBody: InitiateSignMessageRequest,
    ): Response<NetworkResponse<List<InitiateSignMessageResponse>>>
}
