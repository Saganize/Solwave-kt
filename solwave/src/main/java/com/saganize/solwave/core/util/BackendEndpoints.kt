package com.saganize.solwave.core.util

import com.saganize.solwave.BuildConfig

object BackendEndpoints {
    const val BASE_URL: String = BuildConfig.BASE_URL

    object Auth {
        const val INITIATE_LOGIN = BuildConfig.INITIATE_LOGIN
        const val INITIATE_CREATION = BuildConfig.INITIATE_CREATION
    }

    object Transaction {
        const val INITIATE_TRANSACTION = BuildConfig.INITIATE_TRANSACTION
        const val SIMULATE_TRANSACTION = BuildConfig.SIMULATE_TRANSACTION
        const val SIGN_MESSAGE = BuildConfig.SIGN_MESSAGE
    }
}
