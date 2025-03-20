package com.example.weather_app

import com.example.weather_app.Models.FullData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast")
    fun getWeatherDatanow(
        @Query("location") location: String,
        @Query("apikey") apiKey: String,
    ): Call<FullData>

}