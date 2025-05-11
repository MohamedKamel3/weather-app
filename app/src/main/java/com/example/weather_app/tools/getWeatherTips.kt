package com.example.weather_app.tools

import android.content.Context
import com.example.weather_app.Models.WeatherData

fun getWeatherTips(context: Context, weather: WeatherData): List<String> {
    val tips = mutableListOf<String>()

    // Temperature-based tips
    when {
        weather.tempCelsius.toDouble() < 0 -> {
            tips.add("Wear heavy winter clothing including gloves, a scarf, and a hat.")
            tips.add("Limit time spent outdoors to avoid frostbite.")
            tips.add("Watch out for icy roads and sidewalks.")
        }

        weather.tempCelsius.toDouble() in 0.0..15.0 -> {
            tips.add("Wear a jacket or a warm sweater.")
            tips.add("Layer clothing for added comfort and flexibility.")
            tips.add("It might feel colder with wind; wear windproof outerwear.")
        }

        weather.tempCelsius.toDouble() in 15.1..25.0 -> {
            tips.add("Light clothing is suitable for this weather.")
            tips.add("Stay hydrated and wear breathable fabrics.")
            tips.add("Take short breaks in the shade if outdoors for long.")
        }

        weather.tempCelsius.toDouble() > 25 -> {
            tips.add("Wear light, breathable clothing and avoid dark colors.")
            tips.add("Drink plenty of water to stay hydrated.")
            tips.add("Avoid strenuous activities during peak sun hours (12–3 PM).")
        }
    }

    // Status code-based tips
    when (weather.statusCode) {
        // Clear/Sunny
        1000 -> {
            tips.add("Wear sunglasses and a hat to protect against UV rays.")
            tips.add("Apply sunscreen with SPF 30+ even if it doesn't feel hot.")
        }

        // Rain
        in listOf(4000, 4001, 4200, 4201) -> {
            tips.add("Carry a waterproof umbrella or raincoat.")
            tips.add("Wear waterproof shoes to avoid soggy socks.")
            tips.add("Watch for slippery surfaces and puddles.")
        }

        // Cloudy
        in listOf(1101, 1102, 1001) -> {
            tips.add("Expect cooler temperatures; carry a light jacket.")
            tips.add("Sunlight may still be strong; sunscreen is still useful.")
        }

        // Snow
        in listOf(5000, 5001, 5100, 5101) -> {
            tips.add("Wear insulated boots, gloves, and a winter hat.")
            tips.add("Shovel driveways and paths early to prevent ice buildup.")
            tips.add("Avoid unnecessary driving; roads may be slippery.")
        }

        // Thunderstorm
        8000 -> {
            tips.add("Stay indoors to avoid lightning hazards.")
            tips.add("Unplug electronic devices during severe storms.")
            tips.add("Secure loose outdoor items like trash bins or garden furniture.")
        }

        // Freezing Rain
        in listOf(6000, 6001, 6200, 6201) -> {
            tips.add("Use salt or sand on walkways to prevent slipping.")
            tips.add("Avoid driving unless absolutely necessary.")
        }

        // Fog
        in listOf(2000, 2100) -> {
            tips.add("Drive slowly with low-beam headlights.")
            tips.add("Avoid sudden braking or lane changes.")
            tips.add("Use fog lights if available.")
        }

        // Ice Pellets
        in listOf(7000, 7101, 7102) -> {
            tips.add("Avoid going outside during heavy ice pellet fall.")
            tips.add("Protect your eyes with glasses or goggles.")
            tips.add("Stay alert for slippery spots and drive cautiously.")
        }
    }

    return tips
}