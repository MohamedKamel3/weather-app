package com.example.weather_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.weather_app.Composable.SearchScreen
import com.example.weather_app.dataBase.CityDatabase
import com.example.weather_app.ui.theme.Weather_appTheme

class SearchPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Weather_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val database = CityDatabase.getInstance(this)

                    SearchScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        database = database
                    )
                }
            }
        }
    }
}