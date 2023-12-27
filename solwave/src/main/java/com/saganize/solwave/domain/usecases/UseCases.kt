package com.saganize.solwave.domain.usecases

data class UseCases(
    val getWallets: GetWallets,
    val getBalance: GetBalance,
    val getLatestBlockHash: GetLatestBlockHash,
    val saveWallet: SaveWallet,
    val saveWalletPreference: SaveWalletPreference,
    val getWalletsPreference: GetWalletsPreference,
    val saveEncryptionKeyPair: SaveEncryptionKeyPair,
    val getEncryptionKeyPair: GetEncryptionKeyPair,
    val simulateTransaction: SimulateTransaction,
    val initiateCreateUser: InitiateCreateUser,
    val initiateLogin: InitiateLogin,
    val initiateTransaction: InitiateTransaction,
    val initiateSignMessage: InitiateSignMessage,
    val generateSignMessage: GenerateSignMessage,
)
