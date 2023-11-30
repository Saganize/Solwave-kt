package com.saganize.solwave.data.remote.model.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InitiateTransactionRequest(
    @SerialName("publicKey")
    val publicKey: String,
)
