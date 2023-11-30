package com.saganize.solwave.domain.model

import com.saganize.solwave.data.remote.model.SimulationType
import com.saganize.solwave.data.remote.model.TransactionStatus
import com.solana.core.Transaction

// TODO: when serializing to JSON, convert double to string
// check if we can pass long as lamports
data class TransactionParams(
    val type: SimulationType? = null,
    val status: TransactionStatus? = null,
    val data: TransactionPayload,
)

data class TransactionPayload(
    val transaction: Transaction,
    val from: String? = null,
    val to: String? = null,
    val lamports: Double? = null,
    val fees: Double = 0.0,
)

data class TransactionParamsStringHolder(
    val type: SimulationType? = null,
    val status: TransactionStatus? = null,
    val data: TransactionPayloadStringHolder,
)

data class TransactionPayloadStringHolder(
    val transaction: String,
    val from: String? = null,
    val to: String? = null,
    val lamports: Double? = null,
    val fees: Double = 0.0,
)
