package com.example.weather_app.tools

import SharedPrefHelper
import android.content.Context
import com.example.weather_app.Models.WeatherData

fun addWeatherIfNotExists(context: Context, newWeatherData: WeatherData) {

    val searchHistory = SharedPrefHelper.getWeatherList(context).toMutableList()

    val isCityExists =
        searchHistory.any { it.cityName.equals(newWeatherData.cityName, ignoreCase = true) }

    if (!isCityExists) {
        
        searchHistory.add(newWeatherData)
        SharedPrefHelper.saveWeatherList(context, searchHistory)
    }
}