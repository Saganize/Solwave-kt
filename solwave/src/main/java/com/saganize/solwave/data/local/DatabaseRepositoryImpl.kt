package com.saganize.solwave.data.local

import com.saganize.solwave.domain.model.WalletEntity
import com.saganize.solwave.domain.repository.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseRepositoryImpl(private val dao: SolwaveDao) : DatabaseRepository {
    override suspend fun saveWallet(walletEntity: WalletEntity) = withContext(Dispatchers.IO) {
        dao.saveWallet(walletEntity)
    }

    override suspend fun getWallets(): List<WalletEntity>? = withContext(Dispatchers.IO) {
        dao.getWallet().takeUnless { it.isNullOrEmpty() }
    }
}
