package com.example.weather_app.Composable

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp), // Optional padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Start-aligned back button
            BackButton(context)

            // Spacer to push the Text into the center
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Weather Tips",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                    ),
                    color = Color(0xFF1A237E)
                )
            }

            // Optional spacer or empty box for symmetry
            Spacer(modifier = Modifier.width(48.dp)) // Width of the back button
        }

        Row {
            // Weather Info
            Text(
                text = "${weather.tempCelsius}°C, ${weather.description}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 30.sp
                ),
                color = Color(0xFF424242),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            // Weather Icon
            Image(
                painter = painterResource(weather.icon), // Replace with your image resource
                contentDescription = "Weather Icon",
                modifier = Modifier
                    .padding(start = 10.dp)
                    .size(30.dp),
                contentScale = ContentScale.Crop
            )
        }

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
                        fontSize = 24.sp
                    ),
                    color = Color(0xFF212121),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}