package com.example.weather_app.tools

import com.example.weather_app.Adapters.Forecast_Adapter
import com.example.weather_app.Adapters.TempCard_Adapter
import com.example.weather_app.databinding.ActivityMainBinding

fun changeTemperatureUnits(binding: ActivityMainBinding, isCelsius: Boolean) {

    fun convertTemp(value: String): String {
        val temp = value.filter { it.isDigit() || it == '.' || it == '-' }.toDoubleOrNull()

        if (temp == null) return value  // Return as is if conversion fails

        return if (isCelsius) {
            "${((temp - 32) * 5 / 9).toInt()}°C"
        } else {
            "${((temp * 9 / 5) + 32).toInt()}°F"
        }
    }
    //


    binding.TempValue.text = convertTemp(binding.TempValue.text.toString())
    binding.TempHValue.text =
        "H: ${convertTemp(binding.TempHValue.text.toString().replace("H : ", ""))}"
    binding.TempLValue.text =
        "L: ${convertTemp(binding.TempLValue.text.toString().replace("L : ", ""))}"

    val tempAdapter = binding.TempRV.adapter as? TempCard_Adapter
    tempAdapter?.let {
        it.data.forEachIndexed { index, item ->
            item.temp = convertTemp(item.temp)
            it.notifyItemChanged(index)
        }
    }

    val forecastAdapter = binding.Forecast10DayRV.adapter as? Forecast_Adapter
    forecastAdapter?.let {
        it.data.forEachIndexed { index, item ->
            item.temperature = convertTemp(item.temperature)
            it.notifyItemChanged(index)
        }
    }

}