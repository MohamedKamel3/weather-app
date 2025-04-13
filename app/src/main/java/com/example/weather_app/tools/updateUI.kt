package com.example.weather_app.utils

import android.app.Activity
import android.content.Context
import com.example.weather_app.Adapters.Forecast_Adapter
import com.example.weather_app.Adapters.TempCard_Adapter
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.Models.FullData
import com.example.weather_app.Models.VDaysForecast
import com.example.weather_app.Models.WeatherData
import com.example.weather_app.Models.tempcard
import com.example.weather_app.databinding.ActivityMainBinding
import com.example.weather_app.tools.addWeatherIfNotExists
import com.example.weather_app.tools.getWeatherStatus
import com.example.weather_app.tools.parseDateTime
import getFullLocationName
import getLocationName
import getVisibilityDescription

fun updateUI(
    context: Context,
    binding: ActivityMainBinding,
    weatherData: FullData
) {
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


    // Update UI elements
    binding.TempDayMonth.text = parseDateTime(hourlyData[2].date).formattedDateWithDay
    val weatherStatus = getWeatherStatus(
        hourlyData[2].values.weatherCode.toInt(),
        parseDateTime(hourlyData[2].date).time24
    )
    binding.TempDescribe.text = weatherStatus.first
    binding.TempImage.setImageResource(weatherStatus.third)

    binding.HumidityValue.text = "${minutelyData[2].values.humidity} %"
    binding.WindSpeedValue.text = "${minutelyData[2].values.windSpeed} m/s"
    binding.WindGustValue.text = "${minutelyData[2].values.windGust} m/s"
    binding.WindDirectionValue.text = "${minutelyData[2].values.windDirection}°"
    binding.RainIntensityValue.text = "${minutelyData[2].values.rainIntensity} mm/h"
    binding.VisibilityValue.text = "${minutelyData[2].values.visibility} km"
    binding.VisibilityDesc.text = getVisibilityDescription(minutelyData[2].values.visibility)

    // Get location name asynchronously
    getFullLocationName(context, lat, lon) { locationName ->
        binding.TempLocation.text = locationName
    }

    getLocationName(context, lat, lon) { locationName ->
        val newHistory = WeatherData(
            weatherData,
            locationName,
            hourlyData[2].values.tempCelsius.toInt().toString(),
            hourlyData[2].values.tempFahrenheit.toInt().toString(),
            weatherStatus.first,
            weatherStatus.second
        )
        addWeatherIfNotExists(context, newHistory)
    }

    if (isCelsius) {
        binding.TempValue.text = "${hourlyData[2].values.tempCelsius.toInt()}°C"
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
        binding.TempValue.text = "${hourlyData[2].values.tempFahrenheit.toInt()}°F"
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