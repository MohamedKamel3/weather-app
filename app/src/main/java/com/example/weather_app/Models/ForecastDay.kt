package com.example.weather_app.Models

import com.google.gson.annotations.SerializedName


data class ForecastDay(
    // Temperatures
    @SerializedName("temperature") val tempCelsius: Double,
    @SerializedName("temperatureAvg") val avgTempCelsius: Double,
    @SerializedName("temperatureMax") val maxTempCelsius: Double,
    @SerializedName("temperatureMin") val minTempCelsius: Double,

    // Weather conditions
    val weatherCode: Int,
    val weatherCodeMax: Int?,
    val weatherCodeMin: Int?,

    // Wind
    val windSpeed: Double,
    val windDirection: Double,
    val windGust: Double?,

    // Atmospheric
    val humidity: Double,
    val pressureSeaLevelAvg: Double?,
    val visibility: Double,

    // Precipitation
    val rainIntensity: Double,

    // Celestial events
    val sunriseTime: String?,
    val sunsetTime: String?,
    val moonriseTime: String? = null,
    val moonsetTime: String? = null
) {
    // Calculated properties
    val tempFahrenheit get() = (tempCelsius * 9 / 5) + 32
    val avgTempFahrenheit get() = (avgTempCelsius * 9 / 5) + 32
    val maxTempFahrenheit get() = (maxTempCelsius * 9 / 5) + 32
    val minTempFahrenheit get() = (minTempCelsius * 9 / 5) + 32
}