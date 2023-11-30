package com.saganize.solwave.core.util.extensions

import com.saganize.solwave.core.models.Response.Companion.serverErrorMessage
import retrofit2.Response

inline fun <T> Response<T>.ifSuccess(block: (T?) -> Unit): Response<T> {
    if (isSuccessful) {
        block(body())
    }
    return this
}

inline fun <T> Response<T>.ifError(block: (String) -> Unit): Response<T> {
    if (!isSuccessful) {
        block(errorBody()?.string() ?: serverErrorMessage)
    }
    return this
}
