package com.saganize.solwave.core.models

enum class DeeplinkActionType(val value: String) {
    CONNECT_WALLET("connect_wallet"),
    SIGN_MESSAGE("sign_message"),
    SIGN_AND_SEND_TRANSACTION("sign_and_send_transaction"),
}
