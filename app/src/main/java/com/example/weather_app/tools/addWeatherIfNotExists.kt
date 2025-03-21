package com.example.weather_app.tools

import android.content.Context
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.Models.WeatherData

fun addWeatherIfNotExists(context: Context, newWeatherData: WeatherData) {
    val searchHistory = SharedPrefHelper.getWeatherList(context).toMutableList()

    val existingIndex = searchHistory.indexOfFirst {
        it.cityName.equals(newWeatherData.cityName, ignoreCase = true)
    }

    if (existingIndex != -1) {

        searchHistory[existingIndex] = newWeatherData
    } else {

        searchHistory.add(newWeatherData)
    }

    SharedPrefHelper.saveWeatherList(context, searchHistory)
}