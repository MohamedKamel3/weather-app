package com.example.weather_app.widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.MainActivity
import com.example.weather_app.R
import com.example.weather_app.tools.getWeatherStatus
import com.example.weather_app.tools.isCurrentHour
import com.example.weather_app.tools.parseDateTime
import getFullLocationName

class WeatherWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val isCelsius = SharedPrefHelper.getTemperatureUnit(context)
        val weatherData = SharedPrefHelper.getCurrentLocationWeather(context)
        val now = SharedPrefHelper.getNowCurrentLocationWeather(context)
        var city = ""
        var weatherStatus: Triple<String, Int, Int> = Triple("", 0, 0)
        var unit = if (isCelsius) "°C" else "°F"
        var temp: Double? = null
        var hTemp: Double? = null
        var lTemp: Double? = null
        var time: String = ""
        var icon: Int = R.drawable.a21000_fog_light_small2x

        if (weatherData != null && now != null) {
            var i = 0
            while (true) {
                if (isCurrentHour(weatherData.fullData.timelines.hourly[i].date)) {
                    break
                }
                i++
            }
            getFullLocationName(
                context,
                weatherData.fullData.location.lat,
                weatherData.fullData.location.lon
            ) { locationName ->
                city = locationName
            }

            time = parseDateTime(now.data.date).time24
            weatherStatus = getWeatherStatus(
                weatherData.fullData.timelines.hourly[i].values.weatherCode.toInt(),
                parseDateTime(weatherData.fullData.timelines.hourly[i].date).time24
            )

            icon = weatherStatus.second

            if (isCelsius) {
                temp = now.data.values.tempCelsius
                hTemp = weatherData.fullData.timelines.daily[0].values.maxTempCelsius
                lTemp = weatherData.fullData.timelines.daily[0].values.minTempCelsius
            } else {
                temp = now.data.values.tempFahrenheit
                hTemp = weatherData.fullData.timelines.daily[0].values.maxTempFahrenheit
                lTemp = weatherData.fullData.timelines.daily[0].values.minTempFahrenheit
            }
        }
        // Render the widget with whatever data is available
        provideContent {
            MyContent(city, temp, hTemp, lTemp, weatherStatus.first, icon, unit)
        }
    }

    @Composable
    private fun MyContent(
        city: String?,
        temp: Double?,
        tempH: Double?,
        tempL: Double?,
        status: String?,
        icon: Int?,
        unit: String? = "°C"
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically,
            modifier = GlanceModifier
                .fillMaxSize()
                .background(imageProvider = ImageProvider(R.drawable.day_widget))
                .padding(16.dp)
                .clickable(actionStartActivity<MainActivity>())

        ) {
            // Location
            Text(
                text = city ?: "Unknown",
                style = TextStyle(fontSize = 22.sp),
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(25.dp)
            )

            // Weather Icon Placeholder
            Image(
                provider = ImageProvider(icon ?: R.drawable.a21000_fog_light_small2x),
                contentDescription = "Status Icon",
                modifier = GlanceModifier
                    .size(70.dp, 70.dp)
                    .padding(top = 5.dp)
            )

            // Describe
            Text(
                text = status ?: "Unknown",
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal),
            )

            // Temperature
            Text(
                text = "${temp?.toInt() ?: 0}${unit}",
                style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold),
                modifier = GlanceModifier
                    .padding(top = 5.dp)
            )

            Row {
                // High Temperature
                Text(
                    text = "H:${tempH?.toInt() ?: 0}${unit}",
                    modifier = GlanceModifier.padding(bottom = 8.dp),
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal)

                )
                Spacer(GlanceModifier.width(16.dp))
                // Low Temperature
                Text(
                    text = "L:${tempL?.toInt() ?: 0}${unit}",
                    modifier = GlanceModifier.padding(bottom = 8.dp),
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal)

                )
            }
        }
    }
}