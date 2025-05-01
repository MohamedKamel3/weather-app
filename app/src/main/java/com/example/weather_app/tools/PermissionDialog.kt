package com.example.weather_app.tools

import android.app.AlertDialog
import android.content.Context

class PermissionDialog(private val context: Context, private val onOpenSettings: () -> Unit) {
    fun showDialog() {
        AlertDialog.Builder(context)
            .setTitle("Permission denied")
            .setMessage("You didn't allow the permission. Please allow it from settings.")
            .setPositiveButton("Open settings") { _, _ -> onOpenSettings() }
            .setNegativeButton("Cancel", null)
            .show()
    }
}