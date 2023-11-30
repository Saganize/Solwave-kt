package com.saganize.solwave.data.remote.model.response

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class InitiateAuthResponse(
    @SerialName("userId")
    val userId: String,
    @SerialName("email")
    val email: String,
    @SerialName("authIdempotencyId")
    val authIdempotencyId: String,
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("url")
    val url: String,
)
