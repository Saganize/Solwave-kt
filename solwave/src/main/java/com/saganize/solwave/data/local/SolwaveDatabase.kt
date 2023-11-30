package com.saganize.solwave.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.saganize.solwave.domain.model.WalletEntity

@Database(
    entities = [WalletEntity::class],
    version = 2,
)
abstract class SolwaveDatabase : RoomDatabase() {
    abstract val SolwaveDao: SolwaveDao

    companion object {
        const val Database_Name = "database"
    }
}
