package com.example.weather_app.Models

import com.google.gson.annotations.SerializedName

data class FullData(
    var timelines: TimeLine,
    var location: LocationInfo
)