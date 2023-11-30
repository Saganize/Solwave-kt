package com.saganize.solwave.data.remote.model

import com.google.gson.annotations.SerializedName

enum class TransactionStatus(val value: String) {
    @SerializedName("success")
    SUCCESS("success"),

    @SerializedName("failed")
    FAILED("failed"),
}
