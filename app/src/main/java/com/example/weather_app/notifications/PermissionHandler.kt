package com.example.weather_app.notifications

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.weather_app.tools.PermissionDialog

fun handlePermission(
    activity: ComponentActivity,
    title: String,
    message: String
): ActivityResultLauncher<String> {
    return activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            NotificationHelper.showNotification(activity, title, message)
        } else {
            PermissionDialog(activity) {
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", activity.packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.startActivity(intent)
                activity.finish()
            }.showDialog()
        }
    }
}