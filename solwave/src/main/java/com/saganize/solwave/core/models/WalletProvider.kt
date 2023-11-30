package com.saganize.solwave.core.models

sealed class WalletProvider {
    object Saganize : WalletProvider()
    object Phantom : WalletProvider()
    object Solflare : WalletProvider()
}
