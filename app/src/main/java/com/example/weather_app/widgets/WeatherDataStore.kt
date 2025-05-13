package com.example.weather_app.widgets

import android.content.Context
import androidx.datastore.core.DataStore
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.Models.NowData
import com.example.weather_app.Models.WeatherData
import getFullLocationName
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class WeatherDataStore(private val context: Context) : DataStore<WeatherWidgetState> {
    override val data: Flow<WeatherWidgetState>
        get() = flow {
            val isCelsius = SharedPrefHelper.getTemperatureUnit(context)
            val weatherData = SharedPrefHelper.getCurrentLocationWeather(context)
            val nowData = SharedPrefHelper.getNowCurrentLocationWeather(context)

            var locationName = ""
            weatherData?.let { data ->
                locationName = getFullLocationNameSync(
                    context,
                    data.fullData.location.lat,
                    data.fullData.location.lon
                )
            }

            emit(
                WeatherWidgetState(
                    weatherData = weatherData,
                    nowData = nowData,
                    isCelsius = isCelsius,
                    locationName = locationName
                )
            )
        }

    // Helper function to make async call synchronous for Flow
    private suspend fun getFullLocationNameSync(
        context: Context,
        lat: Double,
        lon: Double
    ): String = suspendCoroutine { continuation ->
        getFullLocationName(context, lat, lon) { name ->
            continuation.resume(name)
        }
    }

    override suspend fun updateData(transform: suspend (WeatherWidgetState) -> WeatherWidgetState): WeatherWidgetState {
        throw NotImplementedError("Read-only data store")
    }
}

data class WeatherWidgetState(
    val weatherData: WeatherData?,
    val nowData: NowData?,
    val isCelsius: Boolean,
    val locationName: String

)