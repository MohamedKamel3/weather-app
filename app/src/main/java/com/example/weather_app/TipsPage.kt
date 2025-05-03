package com.example.weather_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.weather_app.Composable.WeatherTipsScreen
import com.example.weather_app.Models.WeatherData
import com.example.weather_app.ui.theme.Weather_appTheme
import com.google.gson.Gson

class TipsPage : ComponentActivity() {
    private lateinit var nowData: WeatherData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val data = intent?.getStringExtra("DATA")
        nowData = Gson().fromJson(data, WeatherData::class.java)

        setContent {
            Weather_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeatherTipsScreen(
                        this, modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding), nowData
                    )
                }
            }
        }
    }
}