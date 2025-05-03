package com.example.weather_app.utils

import android.app.Activity
import android.content.Context
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.Models.FullData
import com.example.weather_app.Models.VDaysForecast
import com.example.weather_app.Models.WeatherData
import com.example.weather_app.Models.tempcard
import com.example.weather_app.adapters.Forecast_Adapter
import com.example.weather_app.adapters.TempCard_Adapter
import com.example.weather_app.databinding.ActivityMainBinding
import com.example.weather_app.tools.addWeatherIfNotExists
import com.example.weather_app.tools.getWeatherStatus
import com.example.weather_app.tools.isCurrentHour
import com.example.weather_app.tools.isCurrentMinute
import com.example.weather_app.tools.parseDateTime
import getFullLocationName
import getLocationName
import getVisibilityDescription

fun updateUI(
    context: Context,
    binding: ActivityMainBinding,
    weatherData: FullData,
    isLocation: Boolean = false,
    city: String = ""
) {
    var i = 0
    var h = 0
    val tempList = arrayListOf<tempcard>()
    val daysList = arrayListOf<VDaysForecast>()
    val hourlyData = weatherData.timelines.hourly
    val minutelyData = weatherData.timelines.minutely
    val dailyData = weatherData.timelines.daily

    if (hourlyData.size < 3 || minutelyData.size < 3 || dailyData.isEmpty()) {
        binding.TempDayMonth.text = "No Data"
        binding.TempValue.text = "--°"
        binding.TempDescribe.text = "No Weather Info"
        binding.TempLocation.text = "No Location Info"
        return
    }

    val lat = weatherData.location.lat
    val lon = weatherData.location.lon
    val isCelsius = SharedPrefHelper.getTemperatureUnit(context)

    while (true) {
        if (isCurrentHour(hourlyData[i].date)) {
            break
        }
        h++
    }

    // Update UI elements
    binding.TempDayMonth.text = parseDateTime(hourlyData[h].date).formattedDateWithDay
    val weatherStatus = getWeatherStatus(
        hourlyData[h].values.weatherCode.toInt(),
        parseDateTime(hourlyData[h].date).time24
    )
    binding.TempDescribe.text = weatherStatus.first
    binding.TempImage.setImageResource(weatherStatus.third)

    while (true) {
        if (isCurrentMinute(minutelyData[i].date)) {
            break
        }
        i++
    }

    binding.HumidityValue.text = "${minutelyData[i].values.humidity} %"
    binding.WindSpeedValue.text = "${minutelyData[i].values.windSpeed} m/s"
    binding.WindGustValue.text = "${minutelyData[i].values.windGust} m/s"
    binding.WindDirectionValue.text = "${minutelyData[i].values.windDirection}°"
    binding.RainIntensityValue.text = "${minutelyData[i].values.rainIntensity} mm/h"
    binding.VisibilityValue.text = "${minutelyData[i].values.visibility} km"
    binding.VisibilityDesc.text = getVisibilityDescription(minutelyData[i].values.visibility)

    if (city == "") {
        getFullLocationName(context, lat, lon) { locationName ->
            binding.TempLocation.text = locationName
        }
        getLocationName(context, lat, lon) { locationName ->
            val newHistory = WeatherData(
                weatherData,
                locationName,
                hourlyData[h].values.tempCelsius.toInt().toString(),
                hourlyData[h].values.tempFahrenheit.toInt().toString(),
                weatherStatus.first,
                weatherStatus.second,
                isLocation
            )
            SharedPrefHelper.saveCurrentLocationWeather(context, newHistory)
        }
    } else {
        binding.TempLocation.text = city

        val newHistory = WeatherData(
            weatherData,
            city,
            hourlyData[h].values.tempCelsius.toInt().toString(),
            hourlyData[h].values.tempFahrenheit.toInt().toString(),
            weatherStatus.first,
            weatherStatus.second,
            isLocation
        )
        addWeatherIfNotExists(context, newHistory)
    }


    if (isCelsius) {
        binding.TempValue.text = "${hourlyData[h].values.tempCelsius.toInt()}°C"
        binding.TempHValue.text = "H: ${dailyData[0].values.maxTempCelsius.toInt()}°C"
        binding.TempLValue.text = "L: ${dailyData[0].values.minTempCelsius.toInt()}°C"

        // Populate RecyclerView lists
        for (hourly in hourlyData.drop(2).take(24)) {
            tempList.add(
                tempcard(
                    getWeatherStatus(
                        hourly.values.weatherCode.toInt(),
                        parseDateTime(hourly.date).time24
                    ).second,
                    "${hourly.values.tempCelsius.toInt()}°C",
                    parseDateTime(hourly.date).time12
                )
            )
        }
        for (i in 1 until dailyData.size) {
            daysList.add(
                VDaysForecast(
                    getWeatherStatus(
                        dailyData[i].values.weatherCodeMax!!.toInt(),
                        parseDateTime(dailyData[i].date).time24
                    ).second,
                    "${dailyData[i].values.avgTempCelsius.toInt()}°C",
                    parseDateTime(dailyData[i].date).date,
                    parseDateTime(dailyData[i].date).dayName,
                )
            )
        }
    } else {
        binding.TempValue.text = "${hourlyData[h].values.tempFahrenheit.toInt()}°F"
        binding.TempHValue.text = "H: ${dailyData[0].values.maxTempFahrenheit.toInt()}°F"
        binding.TempLValue.text = "L: ${dailyData[0].values.minTempFahrenheit.toInt()}°F"

        // Populate RecyclerView lists
        for (hourly in hourlyData.drop(2).take(24)) {
            tempList.add(
                tempcard(
                    getWeatherStatus(
                        hourly.values.weatherCode.toInt(),
                        parseDateTime(hourly.date).time24
                    ).second,
                    "${hourly.values.tempFahrenheit.toInt()}°F",
                    parseDateTime(hourly.date).time12
                )
            )
        }
        for (i in 1 until dailyData.size) {
            daysList.add(
                VDaysForecast(
                    getWeatherStatus(
                        dailyData[i].values.weatherCodeMax!!.toInt(),
                        parseDateTime(dailyData[i].date).time24
                    ).second,
                    "${dailyData[i].values.avgTempFahrenheit.toInt()}°F",
                    parseDateTime(dailyData[i].date).date,
                    parseDateTime(dailyData[i].date).dayName,
                )
            )
        }
    }

    // Update existing adapters instead of resetting them
    (binding.TempRV.adapter as? TempCard_Adapter)?.apply {
        data.clear()
        data.addAll(tempList)
        notifyDataSetChanged()
    } ?: run {
        binding.TempRV.adapter = TempCard_Adapter(context as Activity, tempList)
    }

    (binding.Forecast10DayRV.adapter as? Forecast_Adapter)?.apply {
        data.clear()
        data.addAll(daysList)
        notifyDataSetChanged()
    } ?: run {
        binding.Forecast10DayRV.adapter = Forecast_Adapter(context as Activity, daysList)
    }
}