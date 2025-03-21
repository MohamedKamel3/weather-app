package com.example.weather_app.network

import android.util.Log
import android.view.View
import com.example.weather_app.Models.FullData
import com.example.weather_app.WeatherService
import com.example.weather_app.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class WeatherRepository(private val apiKey: String, private val binding: ActivityMainBinding) {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.tomorrow.io/v4/weather/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val weatherService = retrofit.create(WeatherService::class.java)

    fun fetchWeatherData(lat: Double, lon: Double, onResult: (FullData?) -> Unit) {
        binding.progressBar.visibility = View.VISIBLE
        weatherService.getWeatherDatanow("$lat, $lon", apiKey)
            .enqueue(object : Callback<FullData> {
                override fun onResponse(call: Call<FullData>, response: Response<FullData>) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        onResult(response.body())
                    } else {
                        onResult(null)
                    }
                }

                override fun onFailure(call: Call<FullData>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    Log.e("WeatherRepository", "Error: ${t.message}")
                    onResult(null)
                }
            })
    }

    fun fetchWeatherByCity(cityName: String, onResult: (FullData?) -> Unit) {
        binding.progressBar.visibility = View.VISIBLE
        weatherService.getWeatherDatanow(cityName, apiKey)
            .enqueue(object : Callback<FullData> {
                override fun onResponse(call: Call<FullData>, response: Response<FullData>) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        onResult(response.body())
                    } else {
                        onResult(null)
                    }
                }

                override fun onFailure(call: Call<FullData>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    Log.e("WeatherRepository", "Error: ${t.message}")
                    onResult(null)
                }
            })
    }

}