package com.saganize.solwave.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.saganize.solwave.domain.model.WalletEntity

@Dao
interface SolwaveDao {
    @Query("SELECT * FROM walletEntity")
    suspend fun getWallet(): List<WalletEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWallet(walletEntity: WalletEntity)
}
