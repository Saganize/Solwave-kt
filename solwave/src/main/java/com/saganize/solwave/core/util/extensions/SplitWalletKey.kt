package com.saganize.solwave.core.util.extensions

fun String.splitWalletKey(): Triple<String, String, String> {
    val keys = this.split("/")
    val publicKey = keys[0]
    val session = keys.getOrNull(1) ?: ""
    val sharedSecret = keys.getOrNull(2) ?: ""
    return Triple(publicKey, session, sharedSecret)
}

fun String.getPublicKey(): String {
    return this.splitWalletKey().first
}

fun String.getSession(): String {
    return this.splitWalletKey().second
}

fun String.getSharedSecret(): String {
    return this.splitWalletKey().third
}
