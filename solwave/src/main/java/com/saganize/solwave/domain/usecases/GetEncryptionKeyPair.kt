package com.saganize.solwave.domain.usecases

import com.saganize.solwave.domain.repository.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetEncryptionKeyPair(
    private val dataStoreRepository: DataStoreRepository,
) : UseCase<Unit, Pair<String, String>?> {
    override suspend fun execute(input: Unit): Pair<String, String>? {
        return withContext(Dispatchers.IO) {
            dataStoreRepository.getKeyPair()
        }
    }
}
