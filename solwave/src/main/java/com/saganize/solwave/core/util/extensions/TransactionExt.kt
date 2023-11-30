package com.saganize.solwave.core.util.extensions

import com.google.gson.GsonBuilder
import com.saganize.solwave.domain.model.TransactionSerializer
import com.solana.core.Transaction

fun Transaction.defaultStringify(): String {
    val gsonBuilder = GsonBuilder()
    gsonBuilder.registerTypeAdapter(Transaction::class.java, TransactionSerializer())
    val gson = gsonBuilder.create()
    return gson.toJson(this)
}

fun Transaction.stringifyCustomSerializer(): String {
    val gsonBuilder = GsonBuilder()
    val gson = gsonBuilder.create()
    gsonBuilder.registerTypeAdapter(Transaction::class.java, TransactionSerializer())
    return gson.toJson(this)
}

fun Transaction.validate(): Boolean {
    return this.instructions.isNotEmpty()
}
