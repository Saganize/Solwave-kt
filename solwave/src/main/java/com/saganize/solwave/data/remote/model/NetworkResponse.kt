package com.saganize.solwave.data.remote.model

import com.google.errorprone.annotations.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class NetworkResponse<T>(
    @SerialName("status")
    val status: String = "",
    @SerialName("data")
    val data: T? = null,
    @SerialName("errors")
    val errors: List<NetworkError>? = null,
)
