package com.saganize.solwave.domain.usecases

import com.saganize.solwave.domain.repository.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveWalletPreference(
    private val dataStoreRepository: DataStoreRepository,
) : UseCase<String, Unit> {
    override suspend fun execute(input: String) {
        return withContext(Dispatchers.IO) {
            dataStoreRepository.saveWallet(input)
        }
    }
}
