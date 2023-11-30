package com.saganize.solwave.solwave.presentation.components

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.saganize.solwave.core.models.Response.Companion.genericErrorMsg

@Composable
fun firebaseSignIn(onDone: (FirebaseUser) -> Unit, onErr: (err: String) -> Unit): () -> Unit {
    fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        try {
            if (result.resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user != null) {
                    onDone(user)
                } else {
                    onErr(genericErrorMsg)
                }
            } else {
                onErr(result.idpResponse?.error?.message ?: genericErrorMsg)
            }
        } catch (e: Exception) {
            onErr(e.message ?: genericErrorMsg)
        }
    }

    val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

    val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .setIsSmartLockEnabled(false)
        .build()

    val activityResultLauncher = rememberLauncherForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) {
        onSignInResult(it)
    }

    return {
        activityResultLauncher.launch(signInIntent)
    }
}
