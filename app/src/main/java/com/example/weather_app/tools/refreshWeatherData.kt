package com.example.weather_app.tools

import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.databinding.ActivitySearchViewBinding
import com.example.weather_app.network.WeatherRepository

fun refreshWeatherData(binding: ActivitySearchViewBinding, apiKey: String) {
    val weatherList = SharedPrefHelper.getWeatherList(binding.root.context).toMutableList()
    var weatherRepository = WeatherRepository(apiKey, binding.root.context)


    for ((index, weatherData) in weatherList.withIndex()) {

        val city = weatherData.cityName

        weatherRepository.fetchWeatherByCity(city) { weatherData ->
            weatherData?.let {
                binding.recycler.adapter?.notifyItemChanged(index)
            }
        }
    }
}