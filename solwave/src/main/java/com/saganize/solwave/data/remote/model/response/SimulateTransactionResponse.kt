package com.saganize.solwave.data.remote.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimulateTransactionResponse(
    @SerialName("status")
    val status: String,
    @SerialName("type")
    val type: Long,
    @SerialName("log")
    val log: String,
    @SerialName("networkFee")
    val networkFee: Double,
    val toAddress: String? = null,
    val fromAddress: String? = null,
    val amount: String? = null,
)
