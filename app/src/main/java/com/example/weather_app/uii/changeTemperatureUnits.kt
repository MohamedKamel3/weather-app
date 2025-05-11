package com.example.weather_app.uii

import android.app.Activity
import com.example.weather_app.Models.FullData
import com.example.weather_app.Models.NowData
import com.example.weather_app.Models.VDaysForecast
import com.example.weather_app.Models.tempcard
import com.example.weather_app.adapters.Forecast_Adapter
import com.example.weather_app.adapters.TempCard_Adapter
import com.example.weather_app.databinding.ActivityMainBinding
import com.example.weather_app.tools.getWeatherStatus
import com.example.weather_app.tools.parseDateTime

fun changeTemperatureUnits(
    binding: ActivityMainBinding,
    isCelsius: Boolean,
    weatherData: FullData,
    nowData: NowData
) {
    val tempList = arrayListOf<tempcard>()
    val daysList = arrayListOf<VDaysForecast>()
    val hourlyData = weatherData.timelines.hourly
    val dailyData = weatherData.timelines.daily

    if (isCelsius) {
        binding.TempValue.text = "${nowData.data.values.tempCelsius.toInt()}°C"
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
        binding.TempValue.text = "${nowData.data.values.tempFahrenheit.toInt()}°F"
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
        binding.TempRV.adapter = TempCard_Adapter(binding.root.context as Activity, tempList)
    }

    (binding.Forecast10DayRV.adapter as? Forecast_Adapter)?.apply {
        data.clear()
        data.addAll(daysList)
        notifyDataSetChanged()
    } ?: run {
        binding.Forecast10DayRV.adapter =
            Forecast_Adapter(binding.root.context as Activity, daysList)
    }
}