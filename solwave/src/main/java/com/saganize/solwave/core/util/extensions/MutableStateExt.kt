package com.saganize.solwave.core.util.extensions

import androidx.compose.runtime.MutableState

fun <T> MutableState<T>.update(update: T.() -> T) {
    value = value.update()
}
