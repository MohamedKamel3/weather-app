package com.example.weather_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.work.WorkManager
import com.example.weather_app.Helpers.LocationHelper
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.Models.FullData
import com.example.weather_app.Models.NowData
import com.example.weather_app.Models.WeatherData
import com.example.weather_app.dataBase.CityDatabase
import com.example.weather_app.databinding.ActivityMainBinding
import com.example.weather_app.notifications.NotificationHelper
import com.example.weather_app.service.WeatherRepository
import com.example.weather_app.tools.getWeatherStatus
import com.example.weather_app.tools.parseDateTime
import com.example.weather_app.utils.applyGradientToTemperatureText
import com.example.weather_app.utils.updateUI
import com.example.weather_app.widgets.notifyWeatherWidgetUpdate
import com.example.weather_app.worker.WeatherWorker
import com.google.gson.Gson
import getFullLocationName

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var locationHelper: LocationHelper
    private lateinit var weatherRepository: WeatherRepository
    private val apiKey = "5mmR90Cwo9k6nOPVpVFxLwPzsMChD6F2"
    private lateinit var weatherDataa: FullData
    private lateinit var nnowData: NowData
    private lateinit var ccity: String
    private lateinit var worker: WorkManager


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

        var isLocation = false
        NotificationHelper.createChannel(this)

        worker = WorkManager.getInstance(this)

        if (WeatherWorker.isWorkScheduled(this)) {
            Log.d("WeatherWorker", "Work scheduled")
        } else {
            WeatherWorker.scheduleWork(this, apiKey)
            Log.d("WeatherWorker", "Work not scheduled")
        }

        binding.locationImage.visibility = View.GONE

        // Initialize database
        val database = CityDatabase.getInstance(this)
        if (!SharedPrefHelper.areCitiesImported(this)) {
            CityDatabase.importCitiesFromExcel(this, database.cityDao(), "cities.xlsx")
        }

        // Get the current unit state
        val isCelsiuss = SharedPrefHelper.getTemperatureUnit(this)

        // Update the button text based on new state
        binding.changeDG.text = if (!isCelsiuss) "°C" else "°F"

        binding.searchButton.isEnabled = false
        binding.tempChangeButton.isEnabled = false
        binding.TempInfo.isEnabled = false

        locationHelper = LocationHelper(this)
        weatherRepository = WeatherRepository(apiKey, this)

        if (intent.hasExtra("FULL_DATA")) {
            val json = intent.getStringExtra("FULL_DATA")
            val cityName = intent.getStringExtra("CITY_NAME") ?: ""
            val nowData = intent.getStringExtra("NOW_DATA")
            json?.let {
                nowData?.let {
                    binding.searchButton.isEnabled = true
                    binding.tempChangeButton.isEnabled = true
                    binding.TempInfo.isEnabled = true
                    val fullData = Gson().fromJson(json, FullData::class.java)
                    val nowDataa = Gson().fromJson(nowData, NowData::class.java)
                    weatherDataa = fullData
                    nnowData = nowDataa
                    ccity = cityName
                    updateUI(this, binding, fullData, nowDataa, city = cityName)
                }
            }
        } else if (intent.hasExtra("CITY_NAME")) {
            val city = intent.getStringExtra("CITY_NAME")
            val cityName: String? = intent.getStringExtra("LAT_LON")

            cityName?.let {
                binding.searchButton.isEnabled = true
                binding.tempChangeButton.isEnabled = true
                binding.TempInfo.isEnabled = true
                binding.progressBar.visibility = View.VISIBLE
                weatherRepository.fetchWeatherData(it) { weatherData, now ->
                    runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                        weatherData?.let {
                            now?.let {

                                weatherDataa = weatherData
                                nnowData = it
                                ccity = city ?: cityName
                                updateUI(
                                    context = this,
                                    binding = binding,
                                    weatherData = weatherData,
                                    nowData = it,
                                    city = city!!,
                                    isLocation = false
                                )
                            }
                        }
                    }
                }
            }
        } else {
            binding.progressBar.visibility = View.VISIBLE
            locationHelper.requestLocationPermission {
                locationHelper.getCurrentLocation { lat, lon ->
                    isLocation = true
                    runOnUiThread {
                        var cityName = ""
                        getFullLocationName(this, lat, lon) { locationName ->
                            cityName = locationName
                        }
                        weatherRepository.fetchWeatherData("$lat, $lon") { weatherData, now ->
                            binding.searchButton.isEnabled = true
                            binding.tempChangeButton.isEnabled = true
                            binding.TempInfo.isEnabled = true
                            binding.progressBar.visibility = View.GONE
                            weatherData?.let {
                                now?.let {
                                    weatherDataa = weatherData
                                    nnowData = it
                                    ccity = cityName
                                    updateUI(this, binding, weatherData, it, isLocation = true)
                                    binding.locationImage.visibility = View.VISIBLE
                                }
                            }
                        }
                    }
                }
            }
        }

        applyGradientToTemperatureText(binding.TempValue)

        binding.searchButton.setOnClickListener {
            binding.searchButton.isEnabled = false
            val i = Intent(this, SearchView::class.java)
            startActivity(i)
            binding.searchButton.isEnabled = true
        }

        binding.TempInfo.setOnClickListener {
            binding.TempInfo.isEnabled = false
            val weatherStatus = getWeatherStatus(
                nnowData.data.values.weatherCode.toInt(),
                parseDateTime(nnowData.data.date).time24
            )
            val weatherDATA = WeatherData(
                weatherDataa,
                nnowData,
                ccity,
                nnowData.data.values.tempCelsius.toInt().toString(),
                nnowData.data.values.tempFahrenheit.toInt().toString(),
                weatherStatus.first,
                nnowData.data.values.weatherCode,
                weatherStatus.second,
            )
            val i = Intent(this, TipsPage::class.java).apply {
                putExtra("DATA", Gson().toJson(weatherDATA))
            }
            startActivity(i)
            binding.TempInfo.isEnabled = true
        }

        binding.tempChangeButton.setOnClickListener {
            binding.tempChangeButton.isEnabled = false
            // Get the current unit state
            val isCelsius = SharedPrefHelper.getTemperatureUnit(this)
            // Toggle the state and update the UI
            val newIsCelsius = !isCelsius
            SharedPrefHelper.saveTemperatureUnit(this, newIsCelsius)  // Save new state

            // Update the button text based on new state
            binding.changeDG.text = if (newIsCelsius) "°F" else "°C"

            // Force immediate UI refresh with existing data
            weatherDataa.let { data ->
                nnowData.let { now ->
                    // Call updateUI which will handle widget update
                    updateUI(
                        this,
                        binding,
                        data,
                        now,
                        isUpdate = true,
                        isLocation = isLocation,
                        city = ccity
                    )
                }
            }
            binding.tempChangeButton.isEnabled = true

            // Trigger widget update
            notifyWeatherWidgetUpdate(this)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Exit App")
        builder.setMessage("Are you sure you want to exit the app?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            dialog.dismiss()
            finishAffinity()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}