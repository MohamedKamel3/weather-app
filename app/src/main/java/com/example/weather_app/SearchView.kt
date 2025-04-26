package com.example.weather_app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_app.Helpers.SharedPrefHelper
import com.example.weather_app.Models.WeatherData
import com.example.weather_app.adapters.WeatherAdapter
import com.example.weather_app.databinding.ActivitySearchViewBinding
import com.example.weather_app.tools.refreshWeatherData
import com.example.weather_app.tools.swipeGesture
import com.example.weather_app.tools.updateSearchViewUI
import com.google.gson.Gson
import java.util.Collections

class SearchView : AppCompatActivity() {

    private lateinit var SearchView: Button
    private lateinit var weatherList: MutableList<WeatherData>
    private lateinit var adapter: WeatherAdapter

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

        val currentLocation = SharedPrefHelper.getCurrentLocationWeather(this)
        updateSearchViewUI(this, binding, currentLocation)

        binding.currentLocationCard.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        // Initialize UI components
        SearchView = binding.SearchED
        SearchView.setOnClickListener {
            val intent = Intent(this, SearchPage::class.java)
            startActivity(intent)
        }

        weatherList = SharedPrefHelper.getWeatherList(this)
        adapter = WeatherAdapter(this, weatherList) { selectedItem ->
            val intent = Intent(this, MainActivity::class.java)
            val json = Gson().toJson(selectedItem.fullData)
            intent.putExtra("FULL_DATA", json)
            intent.putExtra("CITY_NAME", selectedItem.cityName)
            startActivity(intent)
            finish()
        }

        val swipeGesture = object : swipeGesture(this) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition

                Collections.swap(weatherList, fromPos, toPos)
                adapter.notifyItemMoved(fromPos, toPos)
                SharedPrefHelper.saveWeatherList(this@SearchView, weatherList)

                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        adapter.deleteItem(viewHolder.adapterPosition)
                        SharedPrefHelper.saveWeatherList(this@SearchView, weatherList)
                    }
                }
            }
        }
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.recycler)

        binding.recycler.adapter = adapter

        binding.swiperefresh.setOnRefreshListener {
            refreshWeatherData(binding, "qKOYd50CTMxrNbd9jkDyQfRLPqWCQhuk")
            binding.swiperefresh.isRefreshing = false
        }

        // Back button
        binding.backbutton.setOnClickListener {
            finish()
        }
    }


}