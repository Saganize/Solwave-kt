package com.saganize.solwave.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkError(
    @SerialName("field")
    val field: String? = null,
    @SerialName("message")
    val message: String? = null,
)
