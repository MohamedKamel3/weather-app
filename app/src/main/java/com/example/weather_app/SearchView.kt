package com.example.weather_app

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.searchdemo.database.CityDatabase
import com.example.searchdemo.viewmodel.CityViewModel
import com.example.weather_app.Adapters.WeatherAdapter
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.databinding.ActivitySearchViewBinding
import com.google.gson.Gson

class SearchView : AppCompatActivity() {
    private val cityViewModel: CityViewModel by viewModels()
    private lateinit var adapter: ArrayAdapter<String>

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

        val database = CityDatabase.getInstance(this)
        CityDatabase.importCitiesFromExcel(this, database.cityDao(), "cities.xlsx")

        val cityViewModel = ViewModelProvider(this)[CityViewModel::class.java]


        binding.recycler.adapter =
            WeatherAdapter(this, SharedPrefHelper.getWeatherList(this)) {
                val intent = Intent(this, MainActivity::class.java)
                val gson = Gson()
                val json = gson.toJson(it.fullData)
                intent.putExtra("FULL_DATA", json)
                startActivity(intent)
                finish()
            }

        binding.backbutton.setOnClickListener {
            finish()
        }

        binding.SeachED.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableStart = binding.SeachED.compoundDrawables[0] // Left Drawable

                if (drawableStart != null) {
                    val drawableWidth = drawableStart.bounds.width()
                    val touchX = event.rawX

                    // Check if the touch is within the drawableStart area
                    if (touchX <= (binding.SeachED.left + drawableWidth + binding.SeachED.paddingStart)) {
                        val cityName = binding.SeachED.text.toString().trim()
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

        binding.SeachED.setOnItemClickListener { _, _, position, _ ->
            val selectedCity = adapter.getItem(position)
            binding.SeachED.setText(selectedCity) // Auto-fill selected city
        }

    }


}