package com.saganize.solwave.core.util.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast

fun Context.isAppInstalled(packageName: String): Boolean {
    val packageManager: PackageManager = packageManager
    return try {
        val packageInfo = packageManager.getApplicationInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun Context.shareLink(url: String) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(
        sendIntent,
        "Share $url",
    )
    startActivity(shareIntent)
}

fun Context.copyToClipboard(label: String, text: String, onSuccess: () -> Unit = {}) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clip = android.content.ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
    onSuccess()
}

fun Context.showToast(message: String, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, length).show()
}

fun Context.closeActivity() {
    val activity = this as Activity
    activity.finish()
}
