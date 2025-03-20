package com.example.weather_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weather_app.databinding.ActivityMainBinding
import com.example.weather_app.network.WeatherRepository
import com.example.weather_app.utils.LocationHelper
import com.example.weather_app.utils.applyGradientToTemperatureText
import com.example.weather_app.utils.updateUI

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var locationHelper: LocationHelper
    private lateinit var weatherRepository: WeatherRepository
    private val apiKey = "ubVT0xEPW2zXCuo33S3GiAma6u71eCZy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val scale = resources.displayMetrics.density
            val desiredPx = (16 * scale + 0.5f).toInt()
            v.setPadding(
                systemBars.left + desiredPx,
                systemBars.top + desiredPx,
                systemBars.right + desiredPx,
                systemBars.bottom
            )
            insets
        }

        locationHelper = LocationHelper(this)
        weatherRepository = WeatherRepository(apiKey, binding)

        locationHelper.requestLocationPermission {
            locationHelper.getCurrentLocation { lat, lon ->
                weatherRepository.fetchWeatherData(lat, lon) { weatherData ->
                    weatherData?.let { updateUI(this, binding, it, lat, lon) }
                }
            }
        }

        applyGradientToTemperatureText(binding.TempValue)

        binding.searchButton.setOnClickListener {
            val i = Intent(this, SearchView::class.java)
            startActivity(i)
        }
    }
}