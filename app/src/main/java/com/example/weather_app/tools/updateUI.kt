package com.example.weather_app.utils

import android.app.Activity
import android.content.Context
import com.example.weather_app.Adapters.Forecast_Adapter
import com.example.weather_app.Adapters.TempCard_Adapter
import com.example.weather_app.Models.FullData
import com.example.weather_app.Models.VDaysForecast
import com.example.weather_app.Models.tempcard
import com.example.weather_app.databinding.ActivityMainBinding
import com.example.weather_app.tools.getWeatherStatus
import com.example.weather_app.tools.parseDateTime
import getLocationName
import getVisibilityDescription

fun updateUI(
    context: Context,
    binding: ActivityMainBinding,
    weatherData: FullData,
    latitude: Double,
    longitude: Double
) {
    val TempList = arrayListOf<tempcard>()
    val DaysList = arrayListOf<VDaysForecast>()

    if (weatherData.timelines.hourly.size > 2 && weatherData.timelines.minutely.size > 2) {
        binding.TempDayMonth.text =
            parseDateTime(weatherData.timelines.hourly[2].date).formattedDateWithDay
        binding.TempValue.text = "${weatherData.timelines.hourly[2].values.temperature.toInt()}°c"
        binding.TempDescribe.text = getWeatherStatus(
            weatherData.timelines.hourly[2].values.weatherCode.toInt(),
            parseDateTime(weatherData.timelines.hourly[2].date).time24
        ).first
        getLocationName(context, latitude, longitude) { locationName ->
            binding.TempLocation.text = locationName
        }
        binding.TempImage.setImageResource(
            getWeatherStatus(
                weatherData.timelines.hourly[2].values.weatherCode.toInt(),
                parseDateTime(weatherData.timelines.hourly[2].date).time24
            ).second
        )
        binding.HumidityValue.setText("${weatherData.timelines.minutely[2].values.humidity} %")
        binding.WindSpeedValue.setText("${weatherData.timelines.minutely[2].values.windSpeed} m/s")
        binding.WindGustValue.setText("${weatherData.timelines.minutely[2].values.windGust} m/s")
        binding.WindDirectionValue.setText("${weatherData.timelines.minutely[2].values.windDirection}°")
        binding.RainIntensityValue.setText("${weatherData.timelines.minutely[2].values.rainIntensity} mm/h")
        binding.VisibilityValue.setText("${weatherData.timelines.minutely[2].values.visibility} km")
        binding.VisibilityDesc.setText(getVisibilityDescription(weatherData.timelines.minutely[2].values.visibility))
        binding.TempHValue.setText("H : ${weatherData.timelines.daily[2].values.tempMax.toInt()}°c")
        binding.TempLValue.setText("L : ${weatherData.timelines.daily[2].values.tempMin.toInt()}°c")

    } else {
        binding.TempDayMonth.text = "No Data"
        binding.TempValue.text = "--°c"
        binding.TempDescribe.text = "No Weather Info"
        binding.TempLocation.text = "No Location Info"
    }
    val currentDate = parseDateTime(weatherData.timelines.hourly[0].date).date
    for (hourlyData in weatherData.timelines.hourly.drop(2)) {
        val hourlyDate = parseDateTime(hourlyData.date).date

        if (hourlyDate == currentDate) {
            TempList.add(
                tempcard(
                    getWeatherStatus(
                        hourlyData.values.weatherCode.toInt(),
                        parseDateTime(hourlyData.date).time24
                    ).second,
                    "${hourlyData.values.temperature.toInt()}°c",
                    parseDateTime(hourlyData.date).time12
                )
            )
        }
    }
    for (dayData in 1 until weatherData.timelines.daily.size) {
        DaysList.add(
            VDaysForecast(
                getWeatherStatus(
                    weatherData.timelines.daily[dayData].values.weatherCodeMax.toInt(),
                    parseDateTime(weatherData.timelines.daily[dayData].date).time24
                ).second,
                "${weatherData.timelines.daily[dayData].values.tempAvg.toInt()}°c",
                parseDateTime(weatherData.timelines.daily[dayData].date).date,
                parseDateTime(weatherData.timelines.daily[dayData].date).dayName,
            )
        )
    }
    binding.TempRV.adapter = TempCard_Adapter(context as Activity, TempList)
    binding.Forecast10DayRV.adapter = Forecast_Adapter(context as Activity, DaysList)
    

}