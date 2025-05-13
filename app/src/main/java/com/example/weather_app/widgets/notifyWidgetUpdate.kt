package com.example.weather_app.widgets

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun notifyWeatherWidgetUpdate(context: Context) {
    CoroutineScope(Dispatchers.IO).launch {
        WeatherWidget.updateAllWidgets(context)
    }
}