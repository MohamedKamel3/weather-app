package com.example.weather_app.Models

import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName

class VDaysForecast(
    @DrawableRes var icon: Int,
    var temperature: String,
    var date: String,
    var day: String,

) {
}