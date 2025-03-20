package com.example.weather_app.Models

import com.google.gson.annotations.SerializedName

data class LocationInfo(
    var lat: Double,
    var lon: Double,
    var name: String,
    var type: String
)