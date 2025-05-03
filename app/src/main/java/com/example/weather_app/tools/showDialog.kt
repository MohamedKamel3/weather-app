package com.example.weather_app.tools

import android.app.AlertDialog
import android.content.Context

fun showDialog(
    context: Context,
    title: String,
    message: String,
    onNegative: () -> Unit?,
    onPositive: () -> Unit
) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("Ok") { _, _ -> onPositive() }
        .setNegativeButton("Cancel") { _, _ -> onNegative() }
        .setCancelable(false)
        .show()
}