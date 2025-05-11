package com.example.weather_app.utils

import android.app.Activity
import android.content.Context
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.Models.FullData
import com.example.weather_app.Models.NowData
import com.example.weather_app.Models.VDaysForecast
import com.example.weather_app.Models.WeatherData
import com.example.weather_app.Models.tempcard
import com.example.weather_app.adapters.Forecast_Adapter
import com.example.weather_app.adapters.TempCard_Adapter
import com.example.weather_app.databinding.ActivityMainBinding
import com.example.weather_app.tools.addWeatherIfNotExists
import com.example.weather_app.tools.getWeatherStatus
import com.example.weather_app.tools.isCurrentHour
import com.example.weather_app.tools.parseDateTime
import getFullLocationName
import getVisibilityDescription

fun updateUI(
    context: Context,
    binding: ActivityMainBinding,
    weatherData: FullData,
    nowData: NowData,
    isLocation: Boolean = false,
    city: String = ""
) {
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
        if (isCurrentHour(hourlyData[h].date)) {
            break
        }
        h++
    }

    // Update UI elements
    binding.TempDayMonth.text = parseDateTime(nowData.data.date).formattedDateWithDay
    val weatherStatus = getWeatherStatus(
        nowData.data.values.weatherCode.toInt(),
        parseDateTime(nowData.data.date).time24
    )
    binding.TempDescribe.text = weatherStatus.first
    binding.TempImage.setImageResource(weatherStatus.third)

    binding.HumidityValue.text = "${nowData.data.values.humidity} %"
    binding.WindSpeedValue.text = "${nowData.data.values.windSpeed} m/s"
    binding.WindGustValue.text = "${nowData.data.values.windGust} m/s"
    binding.WindDirectionValue.text = "${nowData.data.values.windDirection}°"
    binding.RainIntensityValue.text = "${nowData.data.values.rainIntensity} mm/h"
    binding.VisibilityValue.text = "${nowData.data.values.visibility} km"
    binding.VisibilityDesc.text = getVisibilityDescription(nowData.data.values.visibility)

    val unit = if (isCelsius) "°C" else "°F"

    val nowTemp =
        if (isCelsius) nowData.data.values.tempCelsius else nowData.data.values.tempFahrenheit
    val maxTemp =
        if (isCelsius) dailyData[0].values.maxTempCelsius else dailyData[0].values.maxTempFahrenheit
    val minTemp =
        if (isCelsius) dailyData[0].values.minTempCelsius else dailyData[0].values.minTempFahrenheit

    binding.TempValue.text = "${nowTemp.toInt()}$unit"
    binding.TempHValue.text = "H: ${maxTemp.toInt()}$unit"
    binding.TempLValue.text = "L: ${minTemp.toInt()}$unit"

    if (isLocation) {
        getFullLocationName(context, lat, lon) { locationName ->
            binding.TempLocation.text = locationName
            val newHistory = WeatherData(
                weatherData,
                nowData,
                locationName,
                nowData.data.values.tempCelsius.toInt().toString(),
                nowData.data.values.tempFahrenheit.toInt().toString(),
                weatherStatus.first,
                nowData.data.values.weatherCode,
                weatherStatus.second,
                isLocation
            )
            SharedPrefHelper.saveCurrentLocationWeather(context, newHistory)
            SharedPrefHelper.saveNowCurrentLocationWeather(context, nowData)
        }
    } else {
        var newHistory: WeatherData = WeatherData(
            weatherData,
            nowData,
            city,
            nowData.data.values.tempCelsius.toInt().toString(),
            nowData.data.values.tempFahrenheit.toInt().toString(),
            weatherStatus.first,
            nowData.data.values.weatherCode,
            weatherStatus.second,
            isLocation
        )

        if (city == "") {
            getFullLocationName(context, lat, lon) { locationName ->

                val locationNamee = locationName.replace(Regex("[^\\p{L}\\s]"), " ").trim()
                binding.TempLocation.text = locationNamee
                newHistory = WeatherData(
                    weatherData,
                    nowData,
                    locationNamee,
                    nowData.data.values.tempCelsius.toInt().toString(),
                    nowData.data.values.tempFahrenheit.toInt().toString(),
                    weatherStatus.first,
                    nowData.data.values.weatherCode,
                    weatherStatus.second,
                    isLocation
                )
            }
            addWeatherIfNotExists(context, newHistory)
        } else {
            binding.TempLocation.text = city

            newHistory = WeatherData(
                weatherData,
                nowData,
                city,
                nowData.data.values.tempCelsius.toInt().toString(),
                nowData.data.values.tempFahrenheit.toInt().toString(),
                weatherStatus.first,
                nowData.data.values.weatherCode,
                weatherStatus.second,
                isLocation
            )
            addWeatherIfNotExists(context, newHistory)
        }
    }


// Populate hourly list
    hourlyData.drop(2).take(24).forEach { hourly ->
        val temp = if (isCelsius) hourly.values.tempCelsius else hourly.values.tempFahrenheit
        val parsedTime = parseDateTime(hourly.date)
        tempList.add(
            tempcard(
                getWeatherStatus(hourly.values.weatherCode.toInt(), parsedTime.time24).second,
                "${temp.toInt()}$unit",
                parsedTime.time12
            )
        )
    }

// Populate daily forecast list
    for (i in 1 until dailyData.size) {
        val day = dailyData[i]
        val avgTemp = if (isCelsius) day.values.avgTempCelsius else day.values.avgTempFahrenheit
        val parsed = parseDateTime(day.date)
        daysList.add(
            VDaysForecast(
                getWeatherStatus(day.values.weatherCodeMax!!.toInt(), parsed.time24).second,
                "${avgTemp.toInt()}$unit",
                parsed.date,
                parsed.dayName,
            )
        )
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