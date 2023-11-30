package com.saganize.solwave.core.models

data class CompleteEvents(
    val onSuccess: (result: String) -> Unit = {},
    val onFailure: (error: SolwaveErrors) -> Unit = {},
)
