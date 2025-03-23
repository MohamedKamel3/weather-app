package com.example.weather_app.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.weather_app.Models.FullData
import com.example.weather_app.WeatherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// API Keys : 1-(qKOYd50CTMxrNbd9jkDyQfRLPqWCQhuk)   2-(JOjnQGyQdlHRtfnbRF6y2goDoXuw5Rjo)   3-(ubVT0xEPW2zXCuo33S3GiAma6u71eCZy)
class WeatherRepository(private val apiKey: String, private val binding: Context) {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.tomorrow.io/v4/weather/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val weatherService = retrofit.create(WeatherService::class.java)

    fun fetchWeatherData(lat: Double, lon: Double, onResult: (FullData?) -> Unit) {
        weatherService.getWeatherDatanow("$lat, $lon", apiKey)
            .enqueue(object : Callback<FullData> {
                override fun onResponse(call: Call<FullData>, response: Response<FullData>) {
                    if (response.isSuccessful) {
                        onResult(response.body())

                    } else {
                        onResult(null)
                        Toast.makeText(binding, "error happened", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<FullData>, t: Throwable) {
                    Log.e("WeatherRepository", "Error: ${t.message}")
                    onResult(null)
                    Toast.makeText(binding, "error ", Toast.LENGTH_SHORT).show()

                }
            })
    }

    fun fetchWeatherByCity(cityName: String, onResult: (FullData?) -> Unit) {
        weatherService.getWeatherDatanow(cityName, apiKey)
            .enqueue(object : Callback<FullData> {
                override fun onResponse(call: Call<FullData>, response: Response<FullData>) {
                    if (response.isSuccessful) {
                        onResult(response.body())
                    } else {
                        Toast.makeText(binding, "error happened", Toast.LENGTH_SHORT).show()
                        onResult(null)
                    }
                }

                override fun onFailure(call: Call<FullData>, t: Throwable) {
                    Log.e("WeatherRepository", "Error: ${t.message}")
                    Toast.makeText(binding, "error", Toast.LENGTH_SHORT).show()
                    onResult(null)
                }
            })
    }

}