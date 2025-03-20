package com.example.weather_app.Models

import com.google.gson.annotations.SerializedName

data class DayInfo(
    @SerializedName("time") var date: String,
    @SerializedName("values") var values: ForecastDay
)