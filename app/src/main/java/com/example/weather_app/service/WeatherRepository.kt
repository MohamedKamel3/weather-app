package com.example.weather_app.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.weather_app.MainActivity
import com.example.weather_app.Models.ApiErrorResponse
import com.example.weather_app.Models.FullData
import com.example.weather_app.SearchView
import com.example.weather_app.WeatherService
import com.example.weather_app.tools.showDialog
import com.google.gson.Gson
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

    fun fetchWeatherData(
        locationQuery: String,
        onResult: (FullData?) -> Unit
    ) {
        weatherService.getWeatherDatanow(locationQuery, apiKey)
            .enqueue(object : Callback<FullData> {
                override fun onResponse(call: Call<FullData>, response: Response<FullData>) {
                    if (response.isSuccessful) {
                        onResult(response.body())
                    } else {
                        val error = parseErrorBody(response)
                        Log.e("WeatherAPI", "Error ${error?.code}: ${error?.message}")
                        showDialog(
                            binding,
                            "Error",
                            "Error ${error?.code}: ${error?.message}",
                            {
                                val i = Intent(binding, SearchView::class.java)
                                binding.startActivity(i)
                                (binding as? Activity)?.finishAffinity()
                            })
                        {
                            val i = Intent(binding, MainActivity::class.java)
                            binding.startActivity(i)
                            (binding as? Activity)?.finishAffinity()
                        }
                        onResult(null)
                    }
                }

                override fun onFailure(call: Call<FullData>, t: Throwable) {
                    Log.e("WeatherAPI", "Network Failure: ${t.localizedMessage}")
                    Toast.makeText(
                        binding,
                        "Network error: ${t.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                    onResult(null)
                }
            })
    }

    private fun parseErrorBody(response: Response<*>): ApiErrorResponse? {
        return try {
            val errorBody = response.errorBody()?.string()
            val gson = Gson()
            gson.fromJson(errorBody, ApiErrorResponse::class.java)
        } catch (e: Exception) {
            null
        }
    }


}