package com.saganize.solwave.core.util

sealed class PaymentResult {
    object Success : PaymentResult()
    data class Error(val message: String) : PaymentResult()
}
