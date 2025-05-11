package com.example.weather_app.worker

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.Models.WeatherData
import com.example.weather_app.notifications.NotificationHelper
import com.example.weather_app.service.WeatherRepository
import com.example.weather_app.tools.getWeatherStatus
import com.example.weather_app.tools.hasRequiredPermissions
import com.example.weather_app.tools.isCurrentHour
import com.example.weather_app.tools.parseDateTime
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import getFullLocationName
import java.util.concurrent.TimeUnit

class WeatherWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    // Use lazy initialization for dependencies
    private val weatherRepository by lazy {
        WeatherRepository(
            apiKey = inputData.getString(API_KEY_PARAM) ?: "qKOYd50CTMxrNbd9jkDyQfRLPqWCQhuk",
            binding = context
        )
    }
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private val isCelsius = SharedPrefHelper.getTemperatureUnit(context)

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        return try {
            val cancellationTokenSource = CancellationTokenSource()
            var lat: Double = 0.0
            var lon: Double = 0.0
            val context = applicationContext

            // Check if we have all required permissions
            if (!hasRequiredPermissions(context)) {
                return Result.failure()
            }

            // Continue with your work...
            Log.d("WeatherWorker", "Worker started with all permissions")

            // Get last known location instead of requesting new one
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.let { location ->
                        lat = location.latitude
                        lon = location.longitude
                    }
                } else {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            lat = location.latitude
                            lon = location.longitude
                        } else {
                            Toast.makeText(
                                context, "Try Open the Location Settings", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            weatherRepository.fetchWeatherData("$lat,$lon") { weatherData, now ->
                weatherData?.let {
                    now?.let {
                        var i = 0
                        val highTemp: Double
                        val lowTemp: Double
                        var city: String = ""
                        val unit = if (isCelsius) "°C" else "°F"

                        while (true) {
                            if (isCurrentHour(weatherData.timelines.hourly[i].date)) {
                                break
                            }
                            i++
                        }

                        if (isCelsius) {
                            highTemp =
                                weatherData.timelines.daily[0].values.maxTempCelsius
                            lowTemp = weatherData.timelines.daily[0].values.minTempCelsius
                        } else {
                            highTemp =
                                weatherData.timelines.daily[0].values.maxTempFahrenheit
                            lowTemp = weatherData.timelines.daily[0].values.minTempFahrenheit
                        }

                        getFullLocationName(context, lat, lon) { locationName ->
                            city = locationName
                        }

                        val weatherStatus = getWeatherStatus(
                            weatherData.timelines.hourly[i].values.weatherCode.toInt(),
                            parseDateTime(weatherData.timelines.hourly[i].date).time24
                        )
                        val weatherDATA = WeatherData(
                            weatherData,
                            now,
                            city,
                            now.data.values.tempCelsius.toInt().toString(),
                            now.data.values.tempFahrenheit.toInt().toString(),
                            weatherStatus.first,
                            now.data.values.weatherCode,
                            weatherStatus.second,
                        )

                        SharedPrefHelper.saveCurrentLocationWeather(context, weatherDATA)
                        SharedPrefHelper.saveNowCurrentLocationWeather(context, now)

                        NotificationHelper.showNotification(
                            context,
                            weatherDATA,
                            "Weather Alert",
                            "Today's weather: ${weatherStatus.first}.\nHigh: $highTemp$unit, Low: $lowTemp$unit \nclick to see more Tips",
                            weatherStatus.second
                        )
                    }
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("WeatherWorker", "Error in worker", e)
            Result.retry()
        }
    }

    companion object {
        private const val API_KEY_PARAM = "api_key"
        private const val WORK_TAG = "daily_weather_worker"

        fun scheduleWork(context: Context, apiKey: String) {
            val constraints =
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

            // Create input data with API key
            val inputData = Data.Builder().putString(API_KEY_PARAM, apiKey).build()

            // Schedule to run daily at approximately the same time
            val workRequest = PeriodicWorkRequestBuilder<WeatherWorker>(
                15, // Repeat interval
                TimeUnit.MINUTES, 5, // Flex interval
                TimeUnit.MINUTES
            ).setConstraints(constraints).setInputData(inputData).addTag(WORK_TAG).build()

            // Ensure only one instance of this work exists
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_TAG,
                ExistingPeriodicWorkPolicy.REPLACE, // Or REPLACE if you want to update parameters
                workRequest
            )
        }

        fun isWorkScheduled(context: Context): Boolean {
            return WorkManager.getInstance(context).getWorkInfosByTag(WORK_TAG).get()
                .any { workInfo ->
                    workInfo.state == WorkInfo.State.ENQUEUED || workInfo.state == WorkInfo.State.RUNNING
                }
        }

        fun cancelWork(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG)
        }
    }
}