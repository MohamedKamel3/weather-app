package com.example.weather_app.uii

import android.content.Context
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.Models.WeatherData
import com.example.weather_app.databinding.ActivitySearchViewBinding

fun updateSearchViewUI(
    context: Context,
    binding: ActivitySearchViewBinding,
    weatherData: WeatherData?,
) {

    val isCelsius = SharedPrefHelper.getTemperatureUnit(context)


    binding.TemperatureTV.text = if (isCelsius) {
        "${weatherData?.tempCelsius}°C"
    } else {
        "${weatherData?.tempFahrenheit}°F"
    }

    binding.city.setText("${weatherData?.cityName}")

    binding.imgWeatherIcon.setImageResource(weatherData?.icon ?: 0)
}