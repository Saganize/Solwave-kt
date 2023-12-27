package com.saganize.solwave.core.models

import com.saganize.solwave.Solwave

enum class SolwaveErrors(var message: String) {
    NoInternetConnectionMessage(NO_INTERNET),
    GenericErrorMsg(GENERIC_ERROR),
    VerificationErrorMessage(VERIFICATION_FAILED),
    FundsErrorMessage(LESS_BALANCE),
    DeepLinkErrorMessage(DEEP_LINK_ERROR),
    InitCreateUserErrorMessage(INIT_CREATE_USER_ERROR),
    InitLoginErrorMessage(INIT_LOGIN_USER_ERROR),
    WebviewErrorMessage(WEBVIEW_ERROR),
    InitiateTransactionErrorMessage(INITIATE_TRANSACTION_ERROR),
    InitiateSignMessageErrorMessage(INITIATE_SIGN_MESSAGE_ERROR),
    InvalidSigningMessage(INVALID_SIGNING_MESSAGE_LENGTH),
    InvalidTransactionMessage(INVALID_TRANSACTION),
    NoStartEventMessage(NO_START_EVENT),
    NoApiKeyPassedMessage(NO_API_KEY_PASSED),
    NoTransactionPassedMessage(NO_TRANSACTION_PASSED),
    NoMessagePassedMessage(NO_MESSAGE_PASSED),
    NoWalletSelectedMessage(NO_WALLET_SELECTED),
    ;

    fun setError(newError: String): SolwaveErrors {
        message = newError
        return this
    }
}

const val FIREBASE_NOT_INITIALIZED =
    "Firebase is not initialized. Please initialize Firebase before using the library."

const val NO_INTERNET =
    "It looks like your device is offline. Please check your internet connection and try again."

const val GENERIC_ERROR = "Something unexpected happened. Please try again later."

const val VERIFICATION_FAILED = "Transaction signature verification failed. Please try again."

const val LESS_BALANCE =
    "Sorry, we couldn't process your payment due to low wallet balance. Please add funds and try again."
const val DEEP_LINK_ERROR =
    "We encountered an issue with the selected wallet app. Please try again."
const val INIT_CREATE_USER_ERROR =
    "Unable to create user account at the moment. Please try again later."
const val INIT_LOGIN_USER_ERROR =
    "Unable to login user at the moment. Please try again later."
const val INITIATE_TRANSACTION_ERROR =
    "Unable to initiate transaction at the moment. Please try again later."
const val INITIATE_SIGN_MESSAGE_ERROR =
    "Unable to initiate sign message at the moment. Please try again later."
const val INVALID_SIGNING_MESSAGE_LENGTH =
    "Message length is greater than ${Solwave.SIGNING_MESSAGE_MAX_LENGTH}"
const val WEBVIEW_ERROR = GENERIC_ERROR

const val NO_START_EVENT = "No start event found. Illegal state."
const val NO_API_KEY_PASSED = "No API key passed. Please pass API key to use the library."
const val NO_TRANSACTION_PASSED =
    "No transaction passed. Please pass transaction to use the function."
const val NO_MESSAGE_PASSED =
    "No message passed. Please pass message to use the function."

const val INVALID_TRANSACTION = "Invalid transaction. Please pass a valid transaction."

const val NO_WALLET_SELECTED = "No wallet selected. Please select a wallet to continue."
const val NO_USER_FOUND = "This user doesn't exist"
