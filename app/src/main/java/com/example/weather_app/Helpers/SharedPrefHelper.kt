package com.example.weather_app.Helpers

import android.content.Context
import com.example.weather_app.Models.WeatherData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPrefHelper {
    private const val PREFS_NAME = "weather_prefs"
    private const val SEARCH_HISTORY_KEY = "search_history"
    private const val KEY_IS_CELSIUS = "is_celsius"
    private const val KEY_CITIES_IMPORTED = "cities_imported"
    private const val CURRENT_LOCATION_WEATHER_KEY = "current_location_weather"

    fun saveCurrentLocationWeather(context: Context, weatherData: WeatherData) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val json = Gson().toJson(weatherData)
        editor.putString(CURRENT_LOCATION_WEATHER_KEY, json)
        editor.apply()
    }

    fun getCurrentLocationWeather(context: Context): WeatherData? {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPref.getString(CURRENT_LOCATION_WEATHER_KEY, null)

        return if (json != null) {
            Gson().fromJson(json, WeatherData::class.java)
        } else {
            null
        }
    }

    fun saveWeatherList(context: Context, weatherList: MutableList<WeatherData>) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val json = Gson().toJson(weatherList)
        editor.putString(SEARCH_HISTORY_KEY, json)
        editor.apply()
    }

    fun getWeatherList(context: Context): MutableList<WeatherData> {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPref.getString(SEARCH_HISTORY_KEY, "[]") ?: "[]"
        val type = object : TypeToken<MutableList<WeatherData>>() {}.type
        return try {
            Gson().fromJson(json, type) ?: mutableListOf()
        } catch (e: Exception) {
            mutableListOf() // Return empty list on error
        }
    }

    fun saveTemperatureUnit(context: Context, isCelsius: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_IS_CELSIUS, isCelsius).apply()
    }

    fun getTemperatureUnit(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_IS_CELSIUS, true)
    }

    // New: Check if cities have been imported
    fun areCitiesImported(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_CITIES_IMPORTED, false)
    }

    // New: Mark cities as imported
    fun setCitiesImported(context: Context, imported: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_CITIES_IMPORTED, imported).apply()
    }
}