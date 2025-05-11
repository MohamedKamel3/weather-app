package com.example.weather_app.tools

fun checkTimeOfDay(time: String): Boolean {
    val (hourStr, _) = time.split(":")
    val hour = hourStr.toInt()

    return if (hour in 6 until 18) {
        true
    } else
        false

}