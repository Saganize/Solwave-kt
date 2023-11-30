package com.saganize.solwave.core.models

sealed class Response<out T> {
    companion object {
        const val noInternetConnectionMessage = "Device seems to be offline. Please check your internet connection and try again"
        const val genericErrorMsg = "Something went wrong. Please try again."
        const val serverErrorMessage = "Unexpected error on contacting server. Please try again or contact support"
    }

    data class Success<T>(
        val data: T,
    ) : Response<T>()

    data class Error(
        val error: Exception? = null,
    ) : Response<Nothing>() {
        val message: String = error?.message ?: genericErrorMsg

        constructor(message: String) : this(Exception(message))
    }
}

inline fun <T> Response<T>.ifSuccess(block: (T) -> Unit): Response<T> {
    if (this is Response.Success) {
        block(data)
    }
    return this
}

inline fun <T> Response<T>.ifError(block: (String) -> Unit): Response<T> {
    if (this is Response.Error) {
        block(message)
    }
    return this
}
