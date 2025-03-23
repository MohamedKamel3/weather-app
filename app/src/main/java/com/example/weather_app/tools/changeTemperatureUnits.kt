package com.example.weather_app.tools

import android.content.Context
import com.example.weather_app.Adapters.Forecast_Adapter
import com.example.weather_app.Adapters.TempCard_Adapter
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.databinding.ActivityMainBinding

fun toggleTemperatureUnits(context: Context, binding: ActivityMainBinding) {
    val isCelsius = SharedPrefHelper.getTemperatureUnit(context)

    fun convertTemp(value: String): String {
        val temp = value.replace("°c", "").replace("°f", "").trim().toDoubleOrNull()
        return if (temp != null) {
            if (isCelsius) {
                "${((temp * 9 / 5) + 32).toInt()}°f"
            } else {
                "${((temp - 32) * 5 / 9).toInt()}°c"
            }
        } else {
            value
        }
    }

    binding.TempValue.text = convertTemp(binding.TempValue.text.toString())
    binding.TempHValue.text =
        "${convertTemp(binding.TempHValue.text.toString().replace(" H : ", ""))}"
    binding.TempLValue.text =
        "${convertTemp(binding.TempLValue.text.toString().replace(" L : ", ""))}"

    val tempAdapter = binding.TempRV.adapter as? TempCard_Adapter
    tempAdapter?.let {
        for (item in it.data) {
            item.temp = convertTemp(item.temp)
        }
        it.notifyDataSetChanged()
    }

    val forecastAdapter = binding.Forecast10DayRV.adapter as? Forecast_Adapter
    forecastAdapter?.let {
        for (item in it.data) {
            item.temperature = convertTemp(item.temperature)
        }
        it.notifyDataSetChanged()
    }
    SharedPrefHelper.saveTemperatureUnit(context, !isCelsius)
}