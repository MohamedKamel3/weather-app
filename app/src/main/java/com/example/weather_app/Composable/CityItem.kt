package com.example.weather_app.Composable

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather_app.MainActivity


@Composable
fun CityItem(city: String, context: Context) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                val cityName = city.replace(",", "").trim()
                if (cityName.isNotEmpty()) {
                    // Create intent
                    val intent = Intent(context, MainActivity::class.java).apply {
                        putExtra("CITY_NAME", cityName)
                        // Alternative with Gson if you need complex objects:
                        // val gson = Gson()
                        // putExtra("CITY_NAME", gson.toJson(cityName))
                    }
                    context.startActivity(intent)
                    (context as Activity).finish()
                }
            }
    ) {
        Text(
            text = city,
            modifier = Modifier
                .padding(vertical = 8.dp),
            fontSize = 24.sp
        )
        HorizontalDivider()
    }
}