package com.example.weather_app.Helpers

import android.content.Context
import com.example.weather_app.Models.WeatherData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPrefHelper {
    private const val PREFS_NAME = "weather_prefs"
    private const val SEARCH_HISTORY_KEY = "search_history"
    private const val CITY_PREFS_NAME = "city_name"
    private const val KEY_IS_CELSIUS = "is_celsius"


    fun saveWeatherList(context: Context, weatherList: List<WeatherData>) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val json = Gson().toJson(weatherList)
        editor.putString(SEARCH_HISTORY_KEY, json)
        editor.apply()
    }

    fun getWeatherList(context: Context): List<WeatherData> {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPref.getString(SEARCH_HISTORY_KEY, "[]") // Default to empty list
        val type = object : TypeToken<List<WeatherData>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun saveTemperatureUnit(context: Context, isCelsius: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_IS_CELSIUS, isCelsius).apply()
    }

    fun getTemperatureUnit(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_IS_CELSIUS, true)
    }

}