package com.example.weather_app.tools

import com.example.weather_app.Models.TimeInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun parseDateTime(dateTimeString: String): TimeInfo {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")
    val dateTime = LocalDateTime.parse(dateTimeString, formatter)

    val date = dateTime.toLocalDate().toString()  // "2025-02-28"
    val dayName = dateTime.dayOfWeek.getDisplayName(
        java.time.format.TextStyle.FULL,
        Locale.ENGLISH
    ) // "Friday"
    val time24 = dateTime.format(DateTimeFormatter.ofPattern("HH:mm")) // "04:00"
    val time12 =
        dateTime.format(DateTimeFormatter.ofPattern("h a", Locale.ENGLISH))
            .uppercase()
    val formattedDateWithDay = dateTime.format(
        DateTimeFormatter.ofPattern(
            "EEEE, d MMM",
            Locale.ENGLISH
        )
    ) // "Friday, 28 Feb"

    return TimeInfo(date, dayName, time12, time24, formattedDateWithDay)
}