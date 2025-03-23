package com.example.weather_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weather_app.Helpers.LocationHelper
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.Models.FullData
import com.example.weather_app.databinding.ActivityMainBinding
import com.example.weather_app.network.WeatherRepository
import com.example.weather_app.tools.toggleTemperatureUnits
import com.example.weather_app.utils.applyGradientToTemperatureText
import com.example.weather_app.utils.updateUI
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var locationHelper: LocationHelper
    private lateinit var weatherRepository: WeatherRepository
    private val apiKey = "qKOYd50CTMxrNbd9jkDyQfRLPqWCQhuk"
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

        val searchHistory = SharedPrefHelper.getWeatherList(this).toMutableList()

        locationHelper = LocationHelper(this)
        weatherRepository = WeatherRepository(apiKey, binding.root.context)

        if (intent.hasExtra("FULL_DATA")) {
            val json = intent.getStringExtra("FULL_DATA")
            json?.let {
                val fullData = Gson().fromJson(it, FullData::class.java)
                updateUI(this, binding, fullData)
            }
        } else if (intent.hasExtra("CITY_NAME")) {
            val cityName = intent.getStringExtra("CITY_NAME")
            cityName?.let {
                binding.progressBar.visibility = View.VISIBLE  // Show ProgressBar before request
                weatherRepository.fetchWeatherByCity(it) { weatherData ->
                    runOnUiThread {
                        binding.progressBar.visibility = View.GONE  // Hide after response
                        weatherData?.let { updateUI(this, binding, it) }
                    }
                }
            }
        } else {
            binding.progressBar.visibility = View.VISIBLE
            locationHelper.requestLocationPermission {
                locationHelper.getCurrentLocation { lat, lon ->
                    runOnUiThread {
                        weatherRepository.fetchWeatherData(lat, lon) { weatherData ->
                            binding.progressBar.visibility = View.GONE
                            weatherData?.let { updateUI(this, binding, it) }
                        }
                    }
                }
            }
        }

        applyGradientToTemperatureText(binding.TempValue)

        binding.searchButton.setOnClickListener {
            val i = Intent(this, SearchView::class.java)
            startActivity(i)
        }
        binding.tempChangeButton.setOnClickListener {

            val isCelsius = SharedPrefHelper.getTemperatureUnit(this)
            toggleTemperatureUnits(binding.root.context, binding)
            if (isCelsius) {
                binding.changeDG.text = "°c"
            } else binding.changeDG.text = "°f"
        }
    }
}