package com.example.weather_app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weather_app.Helpers.LocationHelper
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.Models.FullData
import com.example.weather_app.databinding.ActivityMainBinding
import com.example.weather_app.network.WeatherRepository
import com.example.weather_app.tools.changeTemperatureUnits
import com.example.weather_app.utils.applyGradientToTemperatureText
import com.example.weather_app.utils.updateUI
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var locationHelper: LocationHelper
    private lateinit var weatherRepository: WeatherRepository
    private val apiKey = "ubVT0xEPW2zXCuo33S3GiAma6u71eCZy"
    private lateinit var weatherDataa: FullData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            installSplashScreen()
        }
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

        // Get the current unit state
        val isCelsiuss = SharedPrefHelper.getTemperatureUnit(this)

        // Update the button text based on new state
        binding.changeDG.text = if (!isCelsiuss) "°C" else "°F"

        binding.searchButton.isEnabled = false
        binding.tempChangeButton.isEnabled = false

        locationHelper = LocationHelper(this)
        weatherRepository = WeatherRepository(apiKey, binding.root.context)


        if (intent.hasExtra("FULL_DATA")) {
            val json = intent.getStringExtra("FULL_DATA")
            json?.let {
                binding.searchButton.isEnabled = true
                binding.tempChangeButton.isEnabled = true
                val fullData = Gson().fromJson(it, FullData::class.java)
                weatherDataa = fullData
                updateUI(this, binding, fullData)
            }
        } else if (intent.hasExtra("CITY_NAME")) {
            val cityName = intent.getStringExtra("CITY_NAME")
            cityName?.let {
                binding.searchButton.isEnabled = true
                binding.tempChangeButton.isEnabled = true
                binding.progressBar.visibility = View.VISIBLE
                weatherRepository.fetchWeatherByCity(it) { weatherData ->
                    runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                        weatherData?.let {
                            weatherDataa = it
                            updateUI(this, binding, it, city = cityName)
                        }
                    }
                }
            }
        } else {
            binding.progressBar.visibility = View.VISIBLE
            locationHelper.requestLocationPermission {
                locationHelper.getCurrentLocation { lat, lon ->
                    runOnUiThread {
                        weatherRepository.fetchWeatherData(lat, lon) { weatherData ->
                            binding.searchButton.isEnabled = true
                            binding.tempChangeButton.isEnabled = true
                            binding.progressBar.visibility = View.GONE
                            weatherData?.let {
                                weatherDataa = it
                                updateUI(this, binding, it, isLocation = true)
                            }
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
            // Get the current unit state
            val isCelsius = SharedPrefHelper.getTemperatureUnit(this)
            // Toggle the state and update the UI
            val newIsCelsius = !isCelsius
            SharedPrefHelper.saveTemperatureUnit(this, newIsCelsius)  // Save new state

            // Update the button text based on new state
            binding.changeDG.text = if (newIsCelsius) "°F" else "°C"

            // Call the function with the new state
            changeTemperatureUnits(binding, newIsCelsius, weatherDataa)
        }

    }
}