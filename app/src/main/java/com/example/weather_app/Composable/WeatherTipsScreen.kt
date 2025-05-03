package com.example.weather_app.Composable

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather_app.Models.WeatherData
import com.example.weather_app.tools.getWeatherTips


@Composable
fun WeatherTipsScreen(context: Context, modifier: Modifier, weather: WeatherData) {
    val tips = getWeatherTips(context, weather)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Weather Tips",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            color = Color(0xFF1A237E),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Weather Info
        Text(
            text = "${weather.tempCelsius}°C, ${weather.description}",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            ),
            color = Color(0xFF424242),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Tips List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            tips.forEachIndexed { index, tip ->
                Text(
                    text = "${index + 1}. $tip",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp
                    ),
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}