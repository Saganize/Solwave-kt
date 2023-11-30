package com.saganize.solwave.domain.repository

interface DataStoreRepository {
    suspend fun saveWallet(value: String)
    suspend fun getWallet(): String?

    suspend fun saveKeyPair(value: Pair<String, String>)
    suspend fun getKeyPair(): Pair<String, String>?
}
