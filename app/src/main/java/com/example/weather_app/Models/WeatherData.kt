package com.example.weather_app.Models

data class WeatherData(
    val fullData: FullData,
    val nowData: NowData,
    val cityName: String,
    var tempCelsius: String,
    val tempFahrenheit: String,
    val description: String,
    val statusCode: Int,
    val icon: Int,
    val isUserLocation: Boolean = false
)