package com.example.weather_app.tools

import com.example.weather_app.Adapters.WeatherAdapter
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.databinding.ActivitySearchViewBinding
import com.example.weather_app.network.WeatherRepository

fun refreshWeatherData(binding: ActivitySearchViewBinding, apiKey: String) {
    val context = binding.root.context
    val weatherList = SharedPrefHelper.getWeatherList(context).toMutableList()
    val weatherRepository = WeatherRepository(apiKey, context)

    for ((index, weatherDataa) in weatherList.withIndex()) {
        val city = weatherDataa.cityName

        weatherRepository.fetchWeatherByCity(city) { weatherData ->
            weatherData?.let {
                // Update the list with a new object
                weatherList[index] = weatherDataa.copy(
                    tempCelsius = "${weatherData.timelines.hourly[2].values.tempCelsius.toInt()}°C",
                    tempFahrenheit = "${weatherData.timelines.hourly[2].values.tempFahrenheit.toInt()}°F"
                )

                // Save the updated list (optional)
                SharedPrefHelper.saveWeatherList(context, weatherList)

                // Ensure the adapter recognizes the update
                binding.recycler.adapter?.let { adapter ->
                    if (adapter is WeatherAdapter) {
                        adapter.updateData(weatherList)  // Call update function in adapter
                    } else {
                        adapter.notifyItemChanged(index)
                    }
                }
            }
        }
    }
}