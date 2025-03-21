package com.example.weather_app.tools

import android.content.Context
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.Models.WeatherData

fun addWeatherIfNotExists(context: Context, newWeatherData: WeatherData) {
    val searchHistory = SharedPrefHelper.getWeatherList(context).toMutableList()

    val existingIndex = searchHistory.indexOfFirst {
        it.cityName.equals(
            newWeatherData.cityName, ignoreCase = true
        ) && (it.fullData.location.lat == newWeatherData.fullData.location.lat && it.fullData.location.lon == newWeatherData.fullData.location.lon)
    }

    if (existingIndex != -1) {
        searchHistory[existingIndex] = newWeatherData // Update existing entry
    } else {
        searchHistory.add(newWeatherData) // Add new entry
    }

    SharedPrefHelper.saveWeatherList(context, searchHistory)
}