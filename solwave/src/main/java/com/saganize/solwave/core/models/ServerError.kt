package com.saganize.solwave.core.models

import com.google.gson.Gson

data class ServerError(
    val data: Any,
    val errors: List<Error>,
    val status: String,
)

// TODO: show the error code to devs
fun String.jsonToError(): String {
    return try {
        when (Gson().fromJson(this, ServerError::class.java).status) {
            "SAGANIZE_TOO_MANY_REQUESTS" -> "Too many requests. Please try again later."
            "SAGANIZE_USER_EXISTS" -> "User already exists. Please try logging in."
            "SAGANIZE_USER_NOT_FOUND" -> "User not found. Please check your email address and try again."
            "SAGANIZE_API_KEY_INVALID" -> SolwaveErrors.GenericErrorMsg.message
            "SAGANIZE_LOGIN_FAILED" -> "Login failed. Please check your credentials and try again."
            "SAGANIZE_TRANSACTION_ERROR" -> "We encountered an issue processing your transaction. Please try again later."
            "SAGANIZE_AUTH_WEBVIEW_EXPIRED" -> SolwaveErrors.GenericErrorMsg.message
            "SAGANIZE_AUTH_WEBVIEW_NOT_FOUND" -> SolwaveErrors.GenericErrorMsg.message
            else -> SolwaveErrors.GenericErrorMsg.message
        }
    } catch (e: Exception) {
        SolwaveErrors.GenericErrorMsg.message
    }
}
