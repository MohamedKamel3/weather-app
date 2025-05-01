package com.example.weather_app.worker

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.weather_app.Helpers.LocationHelper
import com.example.weather_app.network.WeatherRepository
import com.example.weather_app.notifications.NotificationHelper
import com.example.weather_app.notifications.handlePermission
import java.util.concurrent.TimeUnit

class WeatherWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    // Use lazy initialization for dependencies
    private val locationHelper by lazy { LocationHelper(context as AppCompatActivity) }
    private val weatherRepository by lazy {
        WeatherRepository(
            apiKey = inputData.getString(API_KEY_PARAM) ?: "qKOYd50CTMxrNbd9jkDyQfRLPqWCQhuk",
            binding = context
        )
    }

    override suspend fun doWork(): Result {
        return try {
            Log.d("WeatherWorker", "Worker started")
            // 1. Get location
            locationHelper.getCurrentLocation() { lat, lon ->
                Log.d("WeatherWorker", "Location received: $lat, $lon")
                // 2. Fetch weather data
                weatherRepository.fetchWeatherData("Cairo") { weatherData ->
                    weatherData?.let {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            handlePermission(
                                applicationContext as AppCompatActivity,
                                "Weather Alert",
                                "Today's weather: "
                            ).launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            NotificationHelper.showNotification(
                                applicationContext,
                                "Weather Alert",
                                "Today's weather: "
                            )
                        }
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        private const val API_KEY_PARAM = "api_key"
        private const val WORK_TAG = "daily_weather_worker"

        fun scheduleWork(context: Context, apiKey: String) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            // Create input data with API key
            val inputData = Data.Builder()
                .putString(API_KEY_PARAM, apiKey)
                .build()

            // Schedule to run daily at approximately the same time
            val workRequest = PeriodicWorkRequestBuilder<WeatherWorker>(
                15, // Repeat interval
                TimeUnit.MINUTES,
                5, // Flex interval
                TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .setInputData(inputData)
                .addTag(WORK_TAG)
                .build()

            // Ensure only one instance of this work exists
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_TAG,
                ExistingPeriodicWorkPolicy.KEEP, // Or REPLACE if you want to update parameters
                workRequest
            )
        }

        fun isWorkScheduled(context: Context): Boolean {
            return WorkManager.getInstance(context)
                .getWorkInfosByTag(WORK_TAG)
                .get()
                .any { workInfo ->
                    workInfo.state == WorkInfo.State.ENQUEUED ||
                            workInfo.state == WorkInfo.State.RUNNING
                }
        }

        fun cancelWork(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG)
        }
    }
}