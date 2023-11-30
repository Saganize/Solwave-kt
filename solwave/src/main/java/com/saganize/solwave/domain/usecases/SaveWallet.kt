package com.saganize.solwave.domain.usecases

import com.saganize.solwave.domain.model.WalletEntity
import com.saganize.solwave.domain.repository.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveWallet(
    private val databaseRepository: DatabaseRepository,
) : UseCase<WalletEntity, Unit> {
    override suspend fun execute(input: WalletEntity) {
        return withContext(Dispatchers.IO) {
            databaseRepository.saveWallet(
                input,
            )
        }
    }
}
