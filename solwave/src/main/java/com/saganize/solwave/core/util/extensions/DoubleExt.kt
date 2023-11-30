package com.saganize.solwave.core.util.extensions

import com.saganize.solwave.core.util.Constants

fun Double.toSol(): String {
    val x = (this / 1000000000).toFloat()

    return if (x <= 0.00001) "0.00001 " else x.toString()
}

fun Double.formatFee(): String {
    return try {
        if (this < 0.0001) "<0.0001" else String.format("%.4f", this)
    } catch (e: Exception) {
        "<0.0001"
    }
}

fun Double.formatLamportsToSol(): String {
    return try {
        val sol = this / Constants.LAMPORTS_PER_SOL
        if (sol < 0.0001) "<0.0001" else String.format("%.4f", sol)
    } catch (e: Exception) {
        "<0.0001"
    }
}
