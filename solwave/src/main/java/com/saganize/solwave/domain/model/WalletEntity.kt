package com.saganize.solwave.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.saganize.solwave.core.models.WalletInfo
import com.saganize.solwave.core.models.WalletProvider

@Entity(tableName = "walletEntity")
data class WalletEntity(
    @PrimaryKey val name: String,
    val key: String,
) {
    fun toWalletInfo() = WalletInfo(
        walletProvider = when (name) {
            "Solflare" -> WalletProvider.Solflare
            "Phantom" -> WalletProvider.Phantom
            else -> WalletProvider.Saganize
        },
        key = key,
    )
}
