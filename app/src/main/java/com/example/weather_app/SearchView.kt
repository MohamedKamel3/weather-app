package com.example.weather_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.searchdemo.database.CityDatabase
import com.example.weather_app.Adapters.WeatherAdapter
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.databinding.ActivitySearchViewBinding
import com.example.weather_app.tools.refreshWeatherData
import com.google.gson.Gson

class SearchView : AppCompatActivity() {

    private lateinit var SearchView: Button

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivitySearchViewBinding.inflate(layoutInflater)
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

        // Initialize UI components
        SearchView = binding.SearchED
        SearchView.setOnClickListener {
            val intent = Intent(this, SearchPage::class.java)
            startActivity(intent)
        }

        val weatherList = SharedPrefHelper.getWeatherList(this)
        val adapter = WeatherAdapter(this, weatherList) { selectedItem ->
            val intent = Intent(this, MainActivity::class.java)
            val json = Gson().toJson(selectedItem.fullData)
            intent.putExtra("FULL_DATA", json)
            startActivity(intent)
            finish()
        }
        binding.recycler.adapter = adapter

        binding.swiperefresh.setOnRefreshListener {
            refreshWeatherData(binding, "qKOYd50CTMxrNbd9jkDyQfRLPqWCQhuk")
            binding.swiperefresh.isRefreshing = false
        }

        // Initialize database
        val database = CityDatabase.getInstance(this)
        if (!SharedPrefHelper.areCitiesImported(this)) {
            CityDatabase.importCitiesFromExcel(this, database.cityDao(), "cities.xlsx")
        }

        binding.SearchED.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableStart = binding.SearchED.compoundDrawables[0] // Left Drawable

                if (drawableStart != null) {
                    val drawableWidth = drawableStart.bounds.width()
                    val touchX = event.rawX

                    // Check if the touch is within the drawableStart area
                    if (touchX <= (binding.SearchED.left + drawableWidth + binding.SearchED.paddingStart)) {
                        val cityName = binding.SearchED.text.toString().trim()
                        if (cityName.isNotEmpty()) {
                            val intent = Intent(this, MainActivity::class.java)
                            val gson = Gson()
                            val json = gson.toJson(cityName)
                            intent.putExtra("CITY_NAME", json)
                            startActivity(intent)
                            finish()
                        }
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        // Back button
        binding.backbutton.setOnClickListener {
            finish()
        }
    }
}