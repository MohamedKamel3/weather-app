package com.example.weather_app.tools

import android.content.Context
import com.example.weather_app.Models.WeatherData

// Function to generate tips based on weather
fun getWeatherTips(context: Context, weather: WeatherData): List<String> {
    val tips = mutableListOf<String>()
    when {
        weather.tempCelsius.toDouble() < 0 -> {
            tips.add("Wear heavy winter clothing and stay warm.")
            tips.add("Watch out for icy roads and sidewalks.")
        }

        weather.tempCelsius.toDouble() in 0.0..15.0 -> {
            tips.add("A jacket or sweater is recommended.")
            tips.add("Consider layered clothing for comfort.")
        }

        weather.tempCelsius.toDouble() > 15 -> {
            tips.add("Light clothing is suitable for this weather.")
            tips.add("Stay hydrated and use sunscreen if sunny.")
        }
    }

    when (weather.statusCode) {
        // Clear/Sunny weather (1000)
        1000 -> tips.add("Wear sunglasses and a hat for sun protection.")

        // Rain-related weather (4000, 4001, 4200, 4201)
        in listOf(4000, 4001, 4200, 4201) -> {
            tips.add("Bring an umbrella or wear a raincoat.")
            tips.add("Avoid flooded areas and drive carefully.")
        }

        // Cloudy weather (1101, 1102, 1001)
        in listOf(1101, 1102, 1001) -> tips.add("Expect cooler temperatures; carry a light jacket.")

        // Snow-related weather (5000, 5001, 5100, 5101)
        in listOf(5000, 5001, 5100, 5101) -> {
            tips.add("Wear insulated boots and gloves.")
            tips.add("Drive slowly to avoid skidding.")
        }

        // Thunderstorm (8000)
        8000 -> {
            tips.add("Stay indoors if possible to avoid lightning.")
            tips.add("Secure outdoor items against strong winds.")
        }

        // Freezing rain (6000, 6001, 6200, 6201)
        in listOf(6000, 6001, 6200, 6201) -> {
            tips.add("Be extremely careful - surfaces may be icy.")
            tips.add("Avoid unnecessary travel.")
        }

        // Fog (2000, 2100)
        in listOf(2000, 2100) -> {
            tips.add("Use low-beam headlights when driving.")
            tips.add("Allow extra travel time for reduced visibility.")
        }

        // Ice pellets (7000, 7101, 7102)
        in listOf(7000, 7101, 7102) -> {
            tips.add("Wear protective eyewear if going outside.")
            tips.add("Watch for icy patches on walkways.")
        }
    }
    return tips
}