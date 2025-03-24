package com.example.weather_app.Models

data class WeatherData(
    val fullData: FullData,
    val cityName: String,
    var temperature: String,
    val description: String,
    val icon: Int,
    val isUserLocation: Boolean = false
)