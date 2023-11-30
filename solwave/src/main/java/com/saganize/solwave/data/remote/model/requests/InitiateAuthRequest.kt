package com.saganize.solwave.data.remote.model.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InitiateAuthRequest(
    @SerialName("verifyToken")
    val verifyToken: String,
    @SerialName("email")
    val email: String,
)
