package com.saganize.solwave.core.util

import android.content.Context
import android.widget.Toast

@Deprecated(
    message = "Use Context.showToast instead",
    replaceWith = ReplaceWith(
        expression = "this.showToast(message) or context.showToast(message)",
        imports = ["com.saganize.solwave.core.util.showToast"],
    ),
)
fun showToast(context: Context, it: String) {
    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
}
