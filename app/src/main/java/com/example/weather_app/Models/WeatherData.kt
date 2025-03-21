package com.example.weather_app.Models

data class WeatherData(
    val cityName: String,
    val temperature: Int,
    val description: String,
    val icon: Int,
)