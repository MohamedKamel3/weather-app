package com.example.weather_app.tools

import com.example.weather_app.R
import com.example.weather_app.constants.WeatherConstants

fun getWeatherStatus(code: Int, time24: String): Triple<String, Int, Int> {
    val weatherCodes = WeatherConstants.weatherCodes

    val weatherImageMap = WeatherConstants.weatherImageMap

    val weatherBigImageMap = WeatherConstants.weatherBigImageMap

    val description = weatherCodes[code] ?: "Unknown Weather Code"

    val (dayImage, nightImage) = weatherImageMap[code] ?: Pair(null, null)
    val (daybImage, nightbImage) = weatherBigImageMap[code] ?: Pair(null, null)

    val selectedImage = if (time24.split(":")[0].toInt() in 6..18) {
        dayImage
    } else {
        nightImage ?: dayImage
    } ?: R.drawable.ic_launcher_foreground

    val selectedbImage = if (time24.split(":")[0].toInt() in 6..18) {
        daybImage
    } else {
        nightbImage ?: daybImage
    } ?: R.drawable.ic_launcher_foreground

    return Triple(description, selectedImage, selectedbImage)
}