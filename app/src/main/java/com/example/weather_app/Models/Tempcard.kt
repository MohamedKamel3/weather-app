package com.example.weather_app.Models

import androidx.annotation.DrawableRes
import com.google.gson.annotations.SerializedName

class tempcard(
    @DrawableRes @SerializedName("icon")
    var icon: Int,
    @SerializedName("temperature")
    var temp: String,
    var time: String
) {
}