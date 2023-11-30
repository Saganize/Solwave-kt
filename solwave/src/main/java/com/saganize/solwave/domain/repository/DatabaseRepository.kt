package com.saganize.solwave.domain.repository

import com.saganize.solwave.domain.model.WalletEntity

interface DatabaseRepository {
    suspend fun saveWallet(walletEntity: WalletEntity)
    suspend fun getWallets(): List<WalletEntity>?
}
