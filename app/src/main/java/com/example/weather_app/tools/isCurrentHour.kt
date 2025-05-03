package com.example.weather_app.tools

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun isCurrentHour(targetTimeStr: String): Boolean {
    return try {
        val targetTime = LocalDateTime.parse(targetTimeStr, DateTimeFormatter.ISO_DATE_TIME)
        val currentTime = LocalDateTime.now()
        currentTime.year == targetTime.year &&
                currentTime.month == targetTime.month &&
                currentTime.dayOfMonth == targetTime.dayOfMonth &&
                currentTime.hour == targetTime.hour
    } catch (e: Exception) {
        false
    }
}

fun isCurrentMinute(targetTimeStr: String): Boolean {
    return try {
        val targetTime = LocalDateTime.parse(targetTimeStr, DateTimeFormatter.ISO_DATE_TIME)
        val currentTime = LocalDateTime.now()

        currentTime.year == targetTime.year &&
                currentTime.month == targetTime.month &&
                currentTime.dayOfMonth == targetTime.dayOfMonth &&
                currentTime.hour == targetTime.hour &&
                currentTime.minute == targetTime.minute
    } catch (e: Exception) {
        false
    }
}