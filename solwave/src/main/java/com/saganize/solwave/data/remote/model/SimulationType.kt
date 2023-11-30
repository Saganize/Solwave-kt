package com.saganize.solwave.data.remote.model

import com.google.gson.annotations.SerializedName

enum class SimulationType(val value: Int) {
    @SerializedName("transfer")
    TRANSFER(0),

    @SerializedName("other")
    OTHER(1),
}
