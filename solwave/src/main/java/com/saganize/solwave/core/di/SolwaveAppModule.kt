package com.saganize.solwave.core.di

import android.content.Context
import androidx.room.Room
import com.saganize.solwave.core.util.BackendEndpoints.BASE_URL
import com.saganize.solwave.data.local.DataStoreRepositoryImpl
import com.saganize.solwave.data.local.DatabaseRepositoryImpl
import com.saganize.solwave.data.local.SolwaveDatabase
import com.saganize.solwave.data.remote.ApiRepositoryImpl
import com.saganize.solwave.data.remote.SolwaveAPI
import com.saganize.solwave.domain.repository.ApiRepository
import com.saganize.solwave.domain.repository.DataStoreRepository
import com.saganize.solwave.domain.repository.DatabaseRepository
import com.saganize.solwave.domain.usecases.GenerateSignMessage
import com.saganize.solwave.domain.usecases.GetBalance
import com.saganize.solwave.domain.usecases.GetEncryptionKeyPair
import com.saganize.solwave.domain.usecases.GetLatestBlockHash
import com.saganize.solwave.domain.usecases.GetWallets
import com.saganize.solwave.domain.usecases.GetWalletsPreference
import com.saganize.solwave.domain.usecases.InitiateCreateUser
import com.saganize.solwave.domain.usecases.InitiateLogin
import com.saganize.solwave.domain.usecases.InitiateSignMessage
import com.saganize.solwave.domain.usecases.InitiateTransaction
import com.saganize.solwave.domain.usecases.SaveEncryptionKeyPair
import com.saganize.solwave.domain.usecases.SaveWallet
import com.saganize.solwave.domain.usecases.SaveWalletPreference
import com.saganize.solwave.domain.usecases.SimulateTransaction
import com.saganize.solwave.domain.usecases.UseCases
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val TAG = "SOLWAVEAPPTAG"

interface SolwaveAppModule {
    val database: SolwaveDatabase
    val databaseRepository: DatabaseRepository
    val api: SolwaveAPI
    val apiRepository: ApiRepository
    val datastoreRepository: DataStoreRepository
    val usecases: UseCases
}

class SolwaveAppModuleImpl(
    private val appContext: Context,
    private val apiKey: String,
) : SolwaveAppModule {

    // database
    override val database: SolwaveDatabase by lazy {
        Room.databaseBuilder(
            appContext,
            SolwaveDatabase::class.java,
            SolwaveDatabase.Database_Name,
        ).build()
    }
    override val databaseRepository: DatabaseRepository by lazy {
        DatabaseRepositoryImpl(database.SolwaveDao)
    }

    override val api: SolwaveAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SolwaveAPI::class.java)
    }

    override val apiRepository: ApiRepository by lazy {
        ApiRepositoryImpl(api, apiKey)
    }

    override val datastoreRepository: DataStoreRepository by lazy {
        DataStoreRepositoryImpl(appContext)
    }
    override val usecases: UseCases by lazy {
        UseCases(
            GetWallets(databaseRepository),
            GetBalance(),
            GetLatestBlockHash(),
            SaveWallet(databaseRepository),
            SaveWalletPreference(datastoreRepository),
            GetWalletsPreference(datastoreRepository),
            SaveEncryptionKeyPair(datastoreRepository),
            GetEncryptionKeyPair(datastoreRepository),
            SimulateTransaction(apiRepository),
            InitiateCreateUser(apiRepository),
            InitiateLogin(apiRepository),
            InitiateTransaction(apiRepository),
            InitiateSignMessage(apiRepository),
            GenerateSignMessage(),
        )
    }
}
