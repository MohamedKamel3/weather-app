package com.example.weather_app.Models

import com.google.gson.annotations.SerializedName


data class FullData(
    @SerializedName("timelines") val timelines: TimeLine,
    @SerializedName("location") val location: LocationInfo
)