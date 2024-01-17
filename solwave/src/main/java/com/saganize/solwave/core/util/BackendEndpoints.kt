package com.saganize.solwave.core.util

object BackendEndpoints {
    const val BASE_URL: String = "https://staging.saganize.com/api/v1/"

    object Auth {
        const val INITIATE_LOGIN = "solwave/auth/initiateLogin"
        const val INITIATE_CREATION = "solwave/auth/initiateCreateUser"
    }

    object Transaction {
        const val INITIATE_TRANSACTION = "transaction/initiateTransaction"
        const val SIMULATE_TRANSACTION = "transaction/simulate"
        const val SIGN_MESSAGE = "transaction/initiateSignMessage"
    }
}
