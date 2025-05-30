package com.example.weather_app.tools

import android.widget.Toast
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.adapters.WeatherAdapter
import com.example.weather_app.databinding.ActivitySearchViewBinding
import com.example.weather_app.service.WeatherRepository

fun refreshWeatherData(binding: ActivitySearchViewBinding, apiKey: String) {
    val context = binding.root.context
    val weatherList = SharedPrefHelper.getWeatherList(context).toMutableList()
    val weatherRepository = WeatherRepository(apiKey, context)

    for ((index, weatherDataa) in weatherList.withIndex()) {
        val city = weatherDataa.cityName

        weatherRepository.fetchWeatherData(city) { weatherData, now ->
            weatherData?.let {
                now?.let {
                    val weatherStatus = getWeatherStatus(
                        now.data.values.weatherCode.toInt(),
                        parseDateTime(now.data.date).time24
                    )
                    // Update the list with a new object
                    weatherList[index] = weatherDataa.copy(
                        tempCelsius = "${now.data.values.tempCelsius.toInt()}",
                        fullData = weatherData,
                        description = weatherStatus.first,
                        statusCode = now.data.values.weatherCode.toInt(),
                        icon = weatherStatus.second,
                        tempFahrenheit = "${now.data.values.tempFahrenheit.toInt()}"
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
    Toast.makeText(binding.root.context, "Refreshed", Toast.LENGTH_SHORT).show()
}