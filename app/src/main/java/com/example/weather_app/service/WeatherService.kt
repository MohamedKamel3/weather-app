package com.example.weather_app.service

import com.example.weather_app.Models.FullData
import com.example.weather_app.Models.NowData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast")
    fun getWeatherData(
        @Query("location") location: String,
        @Query("apikey") apiKey: String,
    ): Call<FullData>

    @GET("realtime")
    fun getWeatherDataNow(
        @Query("location") location: String,
        @Query("apikey") apiKey: String,
    ): Call<NowData>
}