package com.saganize.solwave.data.remote.model.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InitiateSignMessageRequest(
    @SerialName("publicKey")
    val publicKey: String,
)
