package com.saganize.solwave.core.models

enum class StartEvents(val event: String) {
    PAY("pay"),
    SELECT("select"),
    SIGN_MESSAGE("signMessage"),
}
