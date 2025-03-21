package com.example.weather_app.tools

import SharedPrefHelper
import android.content.Context

fun checkCityExists(context: Context, newWeatherData: String) {

    val searchHistory = SharedPrefHelper.getCityName(context).toMutableList()

    val isCityExists =
        searchHistory.any { it.equals(newWeatherData, ignoreCase = true) }

    if (!isCityExists) {

        searchHistory.add(newWeatherData)
        SharedPrefHelper.saveCityName(context, searchHistory)
    }
}