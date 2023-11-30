package com.saganize.solwave.core.util.extensions

import com.google.gson.GsonBuilder
import com.solana.core.Transaction

fun String.displayWallet(): String {
    return substring(0, 4) + "..." + substring(length - 4)
}

fun String.toTransaction(): Transaction {
    return try {
        if (this.isNotEmpty()) {
            GsonBuilder().create().fromJson(this, Transaction::class.java)
        } else {
            Transaction()
        }
    } catch (e: Exception) {
        Transaction()
    }
}
