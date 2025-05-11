package com.example.weather_app.tools

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat

fun hasRequiredPermissions(context: Context): Boolean {
    val hasLocationPermission = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val hasNotificationPermission =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

    if (!hasLocationPermission || !hasNotificationPermission) {
        Log.w(
            "WeatherWorker",
            "Missing permissions - Location: $hasLocationPermission, Notification: $hasNotificationPermission"
        )
    }

    return hasLocationPermission && hasNotificationPermission
}