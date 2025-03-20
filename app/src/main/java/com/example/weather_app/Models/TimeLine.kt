package com.example.weather_app.Models

data class TimeLine(
    val minutely: List<DayInfo>,
    val hourly: List<DayInfo>,
    val daily: List<DayInfo>,

)