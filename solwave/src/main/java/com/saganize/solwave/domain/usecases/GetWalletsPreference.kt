package com.saganize.solwave.domain.usecases

import com.saganize.solwave.domain.repository.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetWalletsPreference(
    private val dataStoreRepository: DataStoreRepository,
) : UseCase<Unit, String?> {
    override suspend fun execute(input: Unit): String? {
        return withContext(Dispatchers.IO) {
            dataStoreRepository.getWallet()
        }
    }
}
