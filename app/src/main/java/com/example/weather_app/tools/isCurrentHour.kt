package com.example.weather_app.tools

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun isCurrentHour(targetTimeStr: String): Boolean {
    return try {
        val targetTime = ZonedDateTime.parse(targetTimeStr, DateTimeFormatter.ISO_DATE_TIME)
        val currentTime = ZonedDateTime.now(targetTime.zone)
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
        val targetTime = ZonedDateTime.parse(targetTimeStr, DateTimeFormatter.ISO_DATE_TIME)
        val currentTime = ZonedDateTime.now(targetTime.zone)
        currentTime.year == targetTime.year &&
                currentTime.month == targetTime.month &&
                currentTime.dayOfMonth == targetTime.dayOfMonth &&
                currentTime.hour == targetTime.hour &&
                currentTime.minute == targetTime.minute
    } catch (e: Exception) {
        false
    }
}