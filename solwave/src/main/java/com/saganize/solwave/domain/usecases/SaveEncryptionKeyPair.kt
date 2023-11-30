package com.saganize.solwave.domain.usecases

import com.saganize.solwave.domain.repository.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveEncryptionKeyPair(
    private val dataStoreRepository: DataStoreRepository,
) : UseCase<Pair<String, String>, Unit> {
    override suspend fun execute(input: Pair<String, String>) {
        return withContext(Dispatchers.IO) {
            dataStoreRepository.saveKeyPair(input)
        }
    }
}
