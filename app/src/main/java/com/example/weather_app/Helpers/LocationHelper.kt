package com.example.weather_app.Helpers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.weather_app.tools.showDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class LocationHelper(private val context: Context) {

    val activity = context as AppCompatActivity
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    fun requestLocationPermission(onPermissionGranted: () -> Unit) {
        val permissionsToRequest = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        // Only add notification permission for Android 13+ (API 33+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        val locationPermissionRequest = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                onPermissionGranted()
            } else {
                showDialog(
                    activity,
                    "Permission Required",
                    "Location permission is needed to use this feature. Please enable it in app settings.",
                    {
                        activity.finishAffinity()
                    }
                )
                {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", activity.packageName, null)
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.startActivity(intent)
                    activity.finishAffinity()
                }
            }
        }
        locationPermissionRequest.launch(permissionsToRequest.toTypedArray())
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onLocationReceived: (Double, Double) -> Unit) {
        val cancellationTokenSource = CancellationTokenSource()

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.let { location ->
                    val latitude = location.latitude
                    val longitude = location.longitude
                    onLocationReceived(latitude, longitude)
                }
            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        onLocationReceived(latitude, longitude)
                    } else {
                        Toast.makeText(
                            activity,
                            "Try Open the Location Settings",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        }
    }
}