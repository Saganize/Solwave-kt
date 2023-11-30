package com.saganize.solwave.domain.usecases

import com.saganize.solwave.domain.model.WalletEntity
import com.saganize.solwave.domain.repository.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetWallets(
    private val databaseRepository: DatabaseRepository,
) : UseCase<Unit, List<WalletEntity>?> {
    override suspend fun execute(input: Unit): List<WalletEntity>? {
        return withContext(Dispatchers.IO) {
            databaseRepository.getWallets()
        }
    }
}
