package com.saganize.solwave.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InitiateTransactionResponse(
    @SerialName("idempotencyId")
    val idempotencyId: String,
    @SerialName("authToken")
    val authToken: String,
    @SerialName("rsaPublicKey")
    val rsaPublicKey: String,
    @SerialName("url")
    val url: String,
)
