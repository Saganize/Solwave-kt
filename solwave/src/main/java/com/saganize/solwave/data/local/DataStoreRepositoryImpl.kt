package com.saganize.solwave.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.saganize.solwave.domain.repository.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class DataStoreRepositoryImpl(private val context: Context) : DataStoreRepository {

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "datastore1")
        val KEY_STRING = stringPreferencesKey("wallet_key")
        val KEY_PAIR = stringPreferencesKey("key_pair")
    }

    override suspend fun saveWallet(value: String) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences[KEY_STRING] = value
            }
        }
    }
    override suspend fun getWallet(): String? = withContext(Dispatchers.IO) {
        val preferences = context.dataStore.data.firstOrNull()
        preferences?.get(KEY_STRING)
    }

    override suspend fun saveKeyPair(value: Pair<String, String>) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences[KEY_PAIR] = value.first + "/" + value.second
            }
        }
    }
    override suspend fun getKeyPair(): Pair<String, String>? = withContext(Dispatchers.IO) {
        val preferences = context.dataStore.data.firstOrNull()
        val key = preferences?.get(KEY_PAIR)?.split("/")

        if (key.isNullOrEmpty()) {
            null
        } else {
            Pair(key[0], key[1])
        }
    }
}
