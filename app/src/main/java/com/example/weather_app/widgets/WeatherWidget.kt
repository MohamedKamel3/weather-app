package com.example.weather_app.widgets

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
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
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.weather_app.MainActivity
import com.example.weather_app.R
import com.example.weather_app.tools.getWeatherStatus
import com.example.weather_app.tools.parseDateTime
import java.io.File

class WeatherWidget : GlanceAppWidget() {

    override val stateDefinition: GlanceStateDefinition<WeatherWidgetState> =
        object : GlanceStateDefinition<WeatherWidgetState> {
            override suspend fun getDataStore(
                context: Context,
                fileKey: String
            ): DataStore<WeatherWidgetState> {
                return WeatherDataStore(context)
            }

            override fun getLocation(context: Context, fileKey: String): File {
                throw NotImplementedError("Not needed for weather widget")
            }
        }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WeatherWidgetContent(context)
        }
    }

    @Composable
    private fun WeatherWidgetContent(context: Context) {
        // Explicitly specify the type parameter here
        val state = currentState<WeatherWidgetState>()
        val weatherData = state.weatherData
        val nowData = state.nowData
        val isCelsius = state.isCelsius
        var locationName = state.locationName

        if (weatherData == null || nowData == null) {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(ImageProvider(R.drawable.day_widget))
            )
            {
                Text("Loading weather...")
            }
            return
        }


        val weatherStatus = getWeatherStatus(
            nowData.data.values.weatherCode.toInt(),
            parseDateTime(nowData.data.date).time24
        )

        val unit = if (isCelsius) "°C" else "°F"
        val temp =
            if (isCelsius) nowData.data.values.tempCelsius else nowData.data.values.tempFahrenheit
        val maxTemp = if (isCelsius) weatherData.fullData.timelines.daily[0].values.maxTempCelsius
        else weatherData.fullData.timelines.daily[0].values.maxTempFahrenheit
        val minTemp = if (isCelsius) weatherData.fullData.timelines.daily[0].values.minTempCelsius
        else weatherData.fullData.timelines.daily[0].values.minTempFahrenheit

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically,
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ImageProvider(R.drawable.day_widget))
                .padding(16.dp)
                .clickable(actionStartActivity<MainActivity>())
        ) {
            Text(
                text = locationName,
                style = TextStyle(fontSize = 22.sp),
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(25.dp)
            )

            Image(
                provider = ImageProvider(weatherStatus.second),
                contentDescription = "Weather Icon",
                modifier = GlanceModifier
                    .size(70.dp, 70.dp)
                    .padding(top = 5.dp)
            )

            Text(
                text = weatherStatus.first,
                style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal),
            )

            Text(
                text = "${temp.toInt()}$unit",
                style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold),
                modifier = GlanceModifier
                    .padding(top = 5.dp)
            )

            Row {
                Text(
                    text = "H:${maxTemp.toInt()}$unit",
                    modifier = GlanceModifier.padding(bottom = 8.dp),
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal)
                )
                Spacer(GlanceModifier.width(16.dp))
                Text(
                    text = "L:${minTemp.toInt()}$unit",
                    modifier = GlanceModifier.padding(bottom = 8.dp),
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Normal)
                )
            }
        }
    }

    companion object {
        suspend fun updateAllWidgets(context: Context) {
            val manager = GlanceAppWidgetManager(context)
            val widget = WeatherWidget()
            manager.getGlanceIds(WeatherWidget::class.java).forEach { glanceId ->
                widget.update(context, glanceId)
            }
        }
    }
}