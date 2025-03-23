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
    weatherData: FullData,
) {
    val TempList = arrayListOf<tempcard>()
    val DaysList = arrayListOf<VDaysForecast>()
    var lat = 0.0
    var lon = 0.0
    var cityname = ""
    var dec = ""
    var img = 0
    val isCelsius = SharedPrefHelper.getTemperatureUnit(context) // قراءة الوحدة المحفوظة

    if (weatherData.timelines.hourly.size > 2 && weatherData.timelines.minutely.size > 2) {

        lat = weatherData.location.lat
        lon = weatherData.location.lon

        binding.TempDayMonth.text =
            parseDateTime(weatherData.timelines.hourly[2].date).formattedDateWithDay

        val tempC = weatherData.timelines.hourly[2].values.temperature.toInt()
        val tempFormatted = if (isCelsius) "$tempC°c" else "${(tempC * 9 / 5) + 32}°f"
        binding.TempValue.text = tempFormatted

        dec = getWeatherStatus(
            weatherData.timelines.hourly[2].values.weatherCode.toInt(),
            parseDateTime(weatherData.timelines.hourly[2].date).time24
        ).first
        binding.TempDescribe.text = dec

        getFullLocationName(context, lat, lon) { locationName ->
            binding.TempLocation.text = locationName
        }
        getLocationName(context, lat, lon) { locationName ->
            cityname = locationName
        }

        img = getWeatherStatus(
            weatherData.timelines.hourly[2].values.weatherCode.toInt(),
            parseDateTime(weatherData.timelines.hourly[2].date).time24
        ).second
        binding.TempImage.setImageResource(img)

        binding.TempHValue.text =
            "H : ${if (isCelsius) weatherData.timelines.daily[2].values.tempMax.toInt() else ((weatherData.timelines.daily[2].values.tempMax * 9 / 5) + 32).toInt()}°${if (isCelsius) "c" else "f"}"
        binding.TempLValue.text =
            "L : ${if (isCelsius) weatherData.timelines.daily[2].values.tempMin.toInt() else ((weatherData.timelines.daily[2].values.tempMin * 9 / 5) + 32).toInt()}°${if (isCelsius) "c" else "f"}"

        val newhistory = WeatherData(
            weatherData,
            cityname,
            tempC,
            dec,
            img
        )

        binding.HumidityValue.setText("${weatherData.timelines.minutely[2].values.humidity} %")
        binding.WindSpeedValue.setText("${weatherData.timelines.minutely[2].values.windSpeed} m/s")
        binding.WindGustValue.setText("${weatherData.timelines.minutely[2].values.windGust} m/s")
        binding.WindDirectionValue.setText("${weatherData.timelines.minutely[2].values.windDirection}°")
        binding.RainIntensityValue.setText("${weatherData.timelines.minutely[2].values.rainIntensity} mm/h")
        binding.VisibilityValue.setText("${weatherData.timelines.minutely[2].values.visibility} km")
        binding.VisibilityDesc.setText(getVisibilityDescription(weatherData.timelines.minutely[2].values.visibility))

        addWeatherIfNotExists(context, newhistory)
    } else {
        binding.TempDayMonth.text = "No Data"
        binding.TempValue.text = "--°c"
        binding.TempDescribe.text = "No Weather Info"
        binding.TempLocation.text = "No Location Info"
    }

    for (hourlyData in weatherData.timelines.hourly.drop(2).take(24)) {
        val tempC = hourlyData.values.temperature.toInt()
        val tempFormatted = if (isCelsius) "$tempC°c" else "${(tempC * 9 / 5) + 32}°f"

        TempList.add(
            tempcard(
                getWeatherStatus(
                    hourlyData.values.weatherCode.toInt(),
                    parseDateTime(hourlyData.date).time24
                ).second,
                tempFormatted,
                parseDateTime(hourlyData.date).time12
            )
        )
    }

    for (dayData in 1 until weatherData.timelines.daily.size) {
        val tempAvgC = weatherData.timelines.daily[dayData].values.tempAvg.toInt()
        val tempFormatted = if (isCelsius) "$tempAvgC°c" else "${(tempAvgC * 9 / 5) + 32}°f"

        DaysList.add(
            VDaysForecast(
                getWeatherStatus(
                    weatherData.timelines.daily[dayData].values.weatherCodeMax.toInt(),
                    parseDateTime(weatherData.timelines.daily[dayData].date).time24
                ).second,
                tempFormatted,
                parseDateTime(weatherData.timelines.daily[dayData].date).date,
                parseDateTime(weatherData.timelines.daily[dayData].date).dayName,
            )
        )
    }

    binding.TempRV.adapter = TempCard_Adapter(context as Activity, TempList)
    binding.Forecast10DayRV.adapter = Forecast_Adapter(context as Activity, DaysList)
}